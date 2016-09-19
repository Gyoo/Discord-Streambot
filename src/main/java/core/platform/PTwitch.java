package core.platform;

import common.api.twitch.Twitch;
import common.api.twitch.service.entity.*;
import common.api.twitch.service.entity.response.StreamResponse;
import common.api.twitch.service.entity.response.StreamsResponse;
import common.util.HibernateUtil;
import dao.Dao;
import dao.StreamDao;
import entity.*;
import entity.ChannelEntity;
import entity.StreamEntity;
import entity.local.MessageCreateAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ws.discord.messages.MessageHandler;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PTwitch implements Platform {

    private Twitch twitch;
    private Dao dao;
    private StreamDao streamDao;
    private JDA jda;
    private PlatformEntity platform;

    public PTwitch(Dao dao, JDA jda){
        this.twitch = new Twitch("bl2dmxhgd1ots1xlkonz9k0e3mdeboh", "1fih3u5ve67813tj4vebev4u51m8ln");
        this.dao = dao;
        this.streamDao = new StreamDao();
        this.jda = jda;
        platform = dao.getIntId(PlatformEntity.class, 1);
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public void checkStreams(final Long guildID) {
        GuildEntity guild = dao.getLongId(GuildEntity.class, guildID);
        if(guild == null || !guild.isActive()) return;
        if(guild.getChannels().size() == 0 && guild.getGames().size() == 0 && guild.getTeams().size() == 0 && guild.getTags().size() == 0) return;
        if(guild.getGames().size() == 0){
            Set<GameEntity> games = new HashSet<>();
            GameEntity emptyGame = new GameEntity();
            emptyGame.setGuild(guild);
            emptyGame.setName("");
            emptyGame.setPlatform(platform);
            games.add(emptyGame);
            guild.setGames(games);
        }
        for (GameEntity game : guild.getGames()) {
            if(guild.getChannels().size() == 0 && game.getName().equals("")) break;
            if(game.getPlatform().getPlatformId() != platform.getPlatformId()) continue;
            Map<String, String> rp = new HashMap<>();
            rp.put("game", game.getName());
            String channels = "";
            // TODO: Check if userList.size > 100, then send multiple reqeusts
            if (!guild.getChannels().isEmpty()) {
                for (ChannelEntity channel : guild.getChannels()) {
                    if(channel.getPlatform().getPlatformId() != platform.getPlatformId()) continue;
                    channels += "," + channel.getName();
                }
                channels = channels.substring(1);
                rp.put("channel", channels);
            }
            rp.put("limit", "100");
            twitch.getStreamsWithParamsAsync(rp, new Callback<StreamsResponse>() {
                @Override
                public void onResponse(Call<StreamsResponse> call, Response<StreamsResponse> response) {
                    if(response != null && response.body() != null){
                        List<Stream> responses = response.body().getStreams();
                        for (Stream stream : responses) {
                            boolean isDisplayed = false;
                            for(StreamEntity streamEntity : guild.getStreams()){
                                if(streamEntity.getChannelName().equalsIgnoreCase(stream.getChannel().getName())){
                                    isDisplayed = true;
                                    break;
                                }
                            }
                            if(!isDisplayed){
                                updateDiscordList(guild, stream);
                            }
                        }
                        HibernateUtil.getSession().evict(guild);
                    }
                }

                @Override
                public void onFailure(Call<StreamsResponse> call, Throwable t) {

                }
            });

        }
    }

    @Override
    public void checkStillOnline() {
        List<StreamEntity> streams = dao.getAll(StreamEntity.class);
        for(StreamEntity streamEntity : streams){
            if(streamEntity.getPlatform().getPlatformId() == platform.getPlatformId()){
                twitch.getStreamAsync(streamEntity.getChannelName(), new Callback<StreamResponse>() {
                    @Override
                    public void onResponse(Call<StreamResponse> call, Response<StreamResponse> response) {
                        StreamResponse stream = response.body();
                        if(null == stream || null == stream.getStream()){
                            if(streamEntity.getMessageId() != null){
                                MessageHandler.getInstance().addStreamToDeleteQueue(streamEntity);
                            }
                            dao.deleteIntId(StreamEntity.class, streamEntity.getId());
                            System.out.println("[" + LocalDateTime.now().toString() + "] Deleted " + streamEntity.getChannelName());
                        }
                    }

                    @Override
                    public void onFailure(Call<StreamResponse> call, Throwable t) {

                    }
                });
            }
        }
    }

    private void updateDiscordList(GuildEntity guild, Stream stream) {
        String linkBeginning = guild.isCompact() ? "<" : "";
        String linkEnd = guild.isCompact() ? ">" : "";
        if(streamMatchesAttributes(guild, stream)) {
            MessageBuilder builder = new MessageBuilder();
            StreamEntity streamEntity = streamDao.getByIdAndName(guild, stream.getChannel().getName());
            if(streamEntity == null){
                streamEntity = new StreamEntity();
                streamEntity.setPlatform(platform);
                streamEntity.setGuild(guild);
                streamEntity.setChannelName(stream.getChannel().getName().toLowerCase());
            }
            if(stream.getChannel().getStatus() != null) {
                String title = stream.getChannel().getStatus();
                Pattern unicodeOutliers = Pattern.compile("[^\\x00-\\x7F]",
                        Pattern.UNICODE_CASE | Pattern.CANON_EQ
                                | Pattern.CASE_INSENSITIVE);
                Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(title);
                title = unicodeOutlierMatcher.replaceAll(" ");
                streamEntity.setStreamTitle(title);
            }
            streamEntity.setGameName(stream.getGame());
            if(guild.getStreams().contains(streamEntity)){
                guild.getStreams().remove(streamEntity);
            }
            guild.getStreams().add(streamEntity);
            dao.saveOrUpdate(streamEntity);
            System.out.println("[" + LocalDateTime.now().toString() + "] [GUILD : " + jda.getGuildById(Long.toString(guild.getServerId())).getName() + "] " + streamEntity.getChannelName() + " is streaming : " + streamEntity.getStreamTitle() + "| Game : " + stream.getGame());
            for(NotificationEntity notif : guild.getNotifications()){
                switch(Long.toString(notif.getUserId())){
                    case "0":
                        builder.appendEveryoneMention().appendString(" ");
                        break;
                    case "1":
                        builder.appendString("@here ");
                        break;
                    default:
                        User user = jda.getUserById(Long.toString(notif.getUserId()));
                        builder.appendMention(user).appendString(" ");
                        break;
                }
            }
            builder.appendString("NOW LIVE : " + linkBeginning + "http://twitch.tv/" + stream.getChannel().getName() + linkEnd + " playing " + stream.getGame() + " | " + stream.getChannel().getStatus().replaceAll("_", "\\_").replaceAll("~", "\\~").replaceAll("\\*", "\\*"));
            streamEntity = streamDao.getByIdAndName(guild, streamEntity.getChannelName());
            MessageHandler.getInstance().addCreateToQueue(guild.getChannelId(), MessageCreateAction.Type.GUILD, builder.build(), streamEntity);
        }
    }

    /**
     * Checks if stream matches the valid attributes to display a message on the discord server.
     *
     * @param stream The stream to verify
     * @return true if the stream contains one of the tags in this server's tagList
     */
    private boolean streamMatchesAttributes(GuildEntity guild, Stream stream) {
        if (stream == null) return false;

        boolean hasGame = false;
        for(GameEntity game : guild.getGames()){
            if(game.getName().equals("") && guild.getGames().size() == 1){
                hasGame = true;
                break;
            }
            if(game.getPlatform().getPlatformId() == platform.getPlatformId() && game.getName().equalsIgnoreCase(stream.getChannel().getGame())) {
                hasGame = true;
                break;
            }
        }
        if(!hasGame) return false;

        boolean hasTag = guild.getTags().isEmpty();
        List<String> split = new ArrayList<>();
        if (null != stream.getChannel().getStatus()){
            String status = stream.getChannel().getStatus().toLowerCase();
            while(status.contains(" ")){
                split.add(status);
                status = status.substring(status.indexOf(" ") + 1);
            }
            split.add(status);
        }
        for(TagEntity tag : guild.getTags()){
            for(String word : split){
                if(word.startsWith(tag.getName().toLowerCase())){
                    hasTag = true;
                    break;
                }
            }
            if(hasTag) break;
        }
        if (!hasTag) return false;

        return true;
    }
}
