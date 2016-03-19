package ovh.gyoo.bot.data;

import com.mb3364.twitch.api.models.Stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnlineMap {
    private static OnlineMap ourInstance = new OnlineMap();
    private Map<String, List<StreamInfo>> online;

    public static synchronized OnlineMap getInstance() {
        return ourInstance;
    }

    private OnlineMap() {
        online = new HashMap<>();
    }

    public synchronized List<StreamInfo> getStreamList(String serverId){
        return online.get(serverId);
    }

    public synchronized boolean isDisplayed(String serverId, Stream stream) {
        StreamInfo streamInfo = new StreamInfo(stream.getChannel().getName(), stream.getGame(), stream.getChannel().getStatus());
        return online.get(serverId).contains(streamInfo);
    }

    public synchronized void addToList(String serverId, Stream stream){
        StreamInfo streamInfo = new StreamInfo(stream.getChannel().getName(), stream.getGame(), stream.getChannel().getStatus());
        online.get(serverId).add(streamInfo);
    }

    public synchronized void addToList(String serverId, StreamInfo stream){ online.get(serverId).add(stream); }

    public synchronized void removeFromList(String serverId, String name){
        StreamInfo temp = new StreamInfo(name);
        if(online.get(serverId).contains(temp)) online.get(serverId).remove(temp);
    }

    public synchronized void addServer(String serverId){
        online.put(serverId, new ArrayList<>());
    }

    public synchronized Map<String, List<StreamInfo>> getMap(){
        return online;
    }
}
