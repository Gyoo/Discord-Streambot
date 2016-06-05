package core.platform;

import com.mb3364.http.RequestParams;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.handlers.StreamResponseHandler;
import com.mb3364.twitch.api.handlers.StreamsResponseHandler;
import com.mb3364.twitch.api.models.Stream;
import common.util.HibernateUtil;
import dao.Dao;
import dao.StreamDao;
import entity.*;
import entity.local.MessageItem;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import ws.discord.messages.MessageHandler;

import java.util.*;

public class PTwitch implements Platform {

    private Twitch twitch;
    private Dao dao;
    private StreamDao streamDao;
    private JDA jda;
    private PlatformEntity platform;

    public PTwitch(Dao dao, JDA jda){
        this.twitch = new Twitch();
        this.dao = dao;
        this.streamDao = new StreamDao();
        this.jda = jda;
        platform = dao.getIntId(PlatformEntity.class, 1);

        this.twitch.setClientId("bl2dmxhgd1ots1xlkonz9k0e3mdeboh");
        this.twitch.auth().setAccessToken("1fih3u5ve67813tj4vebev4u51m8ln");
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public void checkStreams(final Long guildID) {
        GuildEntity guild = dao.getLongId(GuildEntity.class, guildID);
        if(!guild.isActive()) return;
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
            RequestParams rp = new RequestParams();
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
            rp.put("limit", 100);
            twitch.streams().get(rp, new CustomStreamResponseHandler(dao, jda, guild));
        }
    }

    @Override
    public void checkStillOnline() {
        List<StreamEntity> streams = dao.getAll(StreamEntity.class);
        for(StreamEntity streamEntity : streams){
            if(streamEntity.getPlatform().getPlatformId() == platform.getPlatformId()){
                twitch.streams().get(streamEntity.getChannelName(), new StreamResponseHandler() {
                    @Override
                    public void onSuccess(Stream stream) {
                        if(null == stream || !stream.isOnline()){
                            dao.deleteIntId(StreamEntity.class, streamEntity.getId());
                            System.out.println("Deleted " + streamEntity.getChannelName());
                        }
                    }

                    @Override
                    public void onFailure(int i, String s, String s1) {

                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }
        }
    }

    private void updateDiscordList(GuildEntity guild, Stream stream) {
        String linkBeginning = guild.isCompact() ? "<" : "`";
        String linkEnd = guild.isCompact() ? ">" : " `";
        if(streamMatchesAttributes(guild, stream)) {
            MessageBuilder builder = new MessageBuilder();
            StreamEntity streamEntity = streamDao.getByIdAndName(guild, stream.getChannel().getName());
            if(streamEntity == null){
                streamEntity = new StreamEntity();
                streamEntity.setPlatform(platform);
                streamEntity.setGuild(guild);
                streamEntity.setChannelName(stream.getChannel().getName().toLowerCase());
            }
            streamEntity.setStreamTitle(stream.getChannel().getStatus());
            streamEntity.setGameName(stream.getGame());
            if(guild.getStreams().contains(streamEntity)){
                guild.getStreams().remove(streamEntity);
            }
            guild.getStreams().add(streamEntity);
            dao.saveOrUpdate(streamEntity);
            System.out.println("[GUILD : " + jda.getGuildById(Long.toString(guild.getServerId())).getName() + "] " + streamEntity.getChannelName() + " is streaming : " + streamEntity.getStreamTitle());
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
            builder.appendString("NOW LIVE : " + linkBeginning + "http://twitch.tv/" + stream.getChannel().getName() + linkEnd + " playing " + stream.getGame() + " | " + stream.getChannel().getStatus() + " | (" + stream.getChannel().getBroadcasterLanguage() + ")");
            MessageHandler.getInstance().addToQueue(guild.getChannelId(), MessageItem.Type.GUILD, builder.build());
        }
    }

    /**
     * Checks if stream matches the valid attributes to display a message on the discord server.
     *
     * @param stream The stream to verify
     * @return true if the stream contains one of the tags in this server's tagList
     */
    private boolean streamMatchesAttributes(GuildEntity guild, Stream stream) {
        if (!stream.isOnline()) return false;

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
        List<String> split;
        if (null != stream.getChannel().getStatus())
            split = Arrays.asList(stream.getChannel().getStatus().toLowerCase().split(" "));
        else split = new ArrayList<>();
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

    private class CustomStreamResponseHandler implements StreamsResponseHandler{

        Dao dao;
        JDA jda;
        GuildEntity guild;

        CustomStreamResponseHandler(Dao dao, JDA jda, GuildEntity guild){
            this.dao = dao;
            this.jda = jda;
            this.guild = guild;
        }

        @Override
        public void onSuccess(int i, List<Stream> list) {
            for (Stream stream : list) {
                boolean isDisplayed = false;
                for(StreamEntity streamEntity : guild.getStreams()){
                    if(streamEntity.getChannelName().equalsIgnoreCase(stream.getChannel().getName()) &&
                            (streamEntity.getGameName() == null && stream.getGame() == null || streamEntity.getGameName().equalsIgnoreCase(stream.getGame()))){
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

        @Override
        public void onFailure(int i, String s, String s1) {

        }

        @Override
        public void onFailure(Throwable throwable) {

        }
    }
}
