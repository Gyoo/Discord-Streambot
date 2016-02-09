package ovh.gyoo.bot.handlers;

import com.mb3364.http.RequestParams;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.handlers.ChannelResponseHandler;
import com.mb3364.twitch.api.handlers.StreamResponseHandler;
import com.mb3364.twitch.api.handlers.StreamsResponseHandler;
import com.mb3364.twitch.api.models.Channel;
import com.mb3364.twitch.api.models.Stream;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import ovh.gyoo.bot.data.*;

import java.util.*;

public class TwitchChecker {
    private static TwitchChecker ourInstance = new TwitchChecker();
    private Twitch twitch;

    public static TwitchChecker getInstance() {
        return ourInstance;
    }

    private TwitchChecker() {
        twitch = new Twitch();
        twitch.setClientId("bl2dmxhgd1ots1xlkonz9k0e3mdeboh");
        twitch.auth().setAccessToken("1fih3u5ve67813tj4vebev4u51m8ln");

        //Init OnlineMap
        OnlineMap.getInstance();
    }

    public void checkStreams(boolean startup) {
        List<LocalServer> servers = ServerList.getInstance().getServerList();
        for (final LocalServer server : servers) {
            if (!OnlineMap.getInstance().getMap().containsKey(server.getServerID())) OnlineMap.getInstance().addServer(server.getServerID());
            if (!server.isActive()) continue;
            if (server.getGameList().size() == 0 && server.getUserList().size() == 0) continue;
            if (server.getGameList().size() == 0) server.addGame("");
            for (String game : server.getGameList()) {
                RequestParams rp = new RequestParams();
                rp.put("game", game);
                String channels = "";
                if (server.getUserList().size() > 0) {
                    for (String name : server.getUserList()) {
                        channels += "," + name;
                    }
                    channels = channels.substring(1);
                    rp.put("channel", channels);
                }
                rp.put("limit", 100);
                twitch.streams().get(rp, new StreamsResponseHandler() {
                    @Override
                    public void onSuccess(int i, List<Stream> list) {
                        for (Stream stream : list) {
                            if (stream.isOnline()) {
                                if (server.getTagList().size() > 0) {
                                    boolean hasTag = false;
                                    for (String tag : server.getTagList()) {
                                        if (null != stream.getChannel().getStatus() && stream.getChannel().getStatus().toLowerCase().contains(tag.toLowerCase())) {
                                            hasTag = true;
                                            break;
                                        }
                                    }
                                    if (!hasTag) continue;
                                }
                                if (!OnlineMap.getInstance().getNameList(server.getServerID()).contains(stream.getChannel().getName())) {
                                    OnlineMap.getInstance().addToList(server.getServerID(),stream.getChannel().getName());
                                    if(!startup)DiscordInstance.getInstance().addToQueue(new MessageItem(server.getId(), MessageItem.Type.GUILD, new MessageBuilder()
                                            .appendString("NOW LIVE : `http://twitch.tv/" + stream.getChannel().getName() + " ` playing " + stream.getGame() + " | " + stream.getChannel().getStatus() + " | (" + stream.getChannel().getBroadcasterLanguage() + ")")
                                            .build()));
                                }
                            }
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
            if (server.getGameList().contains("")) server.removeGame("");
        }
    }

    public void checkStillOnline() {
        List<LocalServer> servers = ServerList.getInstance().getServerList();
        for (final LocalServer server : servers) {
            if (OnlineMap.getInstance().getNameList(server.getServerID()).size() > 0) {
                for (final String name : OnlineMap.getInstance().getNameList(server.getServerID()))
                    twitch.streams().get(name, new StreamResponseHandler() {
                        @Override
                        public void onSuccess(Stream stream) {
                            if(null == stream){
                                OnlineMap.getInstance().removeFromList(server.getServerID(), name);
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
}
