package ovh.gyoo.bot.handlers;

import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.handlers.*;
import com.mb3364.twitch.api.models.Stream;
import ovh.gyoo.bot.data.*;

import java.util.*;

public class TwitchChecker {
    private static TwitchChecker ourInstance = new TwitchChecker();
    private static Twitch twitch = null;

    public static TwitchChecker getInstance() {
        return ourInstance;
    }

    private TwitchChecker() {
        if(twitch == null) twitch = new Twitch();
        twitch.setClientId("bl2dmxhgd1ots1xlkonz9k0e3mdeboh");
        twitch.auth().setAccessToken("1fih3u5ve67813tj4vebev4u51m8ln");

        //Init OnlineMap
        OnlineMap.getInstance();
    }

    public static Twitch getTwitch() {
        if(twitch == null) twitch = new Twitch();
        return twitch;
    }

    public void checkStreams(boolean startup) {
        List<LocalServer> servers = ServerList.getInstance().getServerList();
        for (final LocalServer server : servers) {
            if (!OnlineMap.getInstance().getMap().containsKey(server.getServerID())) OnlineMap.getInstance().addServer(server.getServerID());
            server.checkStreams(startup);
        }
    }

    public void checkStillOnline() {
        List<LocalServer> servers = ServerList.getInstance().getServerList();
        for (final LocalServer server : servers) {
            if (OnlineMap.getInstance().getStreamList(server.getServerID()).size() > 0) {
                List<StreamInfo> infos = new ArrayList<>(OnlineMap.getInstance().getStreamList(server.getServerID()));
                for (final StreamInfo streamInfo : infos)
                    twitch.streams().get(streamInfo.getName(), new StreamResponseHandler() {
                        @Override
                        public void onSuccess(Stream stream) {
                            if(null == stream){
                                OnlineMap.getInstance().removeFromList(server.getServerID(), streamInfo.getName());
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
