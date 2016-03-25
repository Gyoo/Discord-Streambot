package ovh.gyoo.bot.data;

import com.mb3364.twitch.api.models.Stream;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class OnlineMap {
    private static OnlineMap ourInstance = new OnlineMap();
    private ConcurrentMap<String, List<StreamInfo>> online;

    public static synchronized OnlineMap getInstance() {
        return ourInstance;
    }

    private OnlineMap() {
        online = new ConcurrentHashMap<>();
    }

    public List<StreamInfo> getStreamList(String serverId){
        return online.get(serverId);
    }

    public boolean isDisplayed(String serverId, Stream stream) {
        StreamInfo streamInfo = new StreamInfo(stream.getChannel().getName(), stream.getGame(), stream.getChannel().getStatus());
        return online.get(serverId).contains(streamInfo);
    }

    public void addToList(String serverId, Stream stream){
        StreamInfo streamInfo = new StreamInfo(stream.getChannel().getName(), stream.getGame(), stream.getChannel().getStatus());
        online.get(serverId).add(streamInfo);
    }

    public void removeFromList(String serverId, String name){
        StreamInfo temp = new StreamInfo(name);
        if (online.get(serverId).contains(temp)) online.get(serverId).remove(temp);
    }

    public void addServer(String serverId){
        online.put(serverId, Collections.synchronizedList(new ArrayList<>()));
    }

    public ConcurrentMap<String, List<StreamInfo>> getMap(){
        return online;
    }
}
