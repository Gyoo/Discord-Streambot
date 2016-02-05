package ovh.gyoo.bot.data;

import com.sun.istack.internal.Nullable;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerList{
    private static ServerList ourInstance = new ServerList();
    private Map<String, LocalServer> servers;
    public synchronized static ServerList getInstance() {
        return ourInstance;
    }

    private ServerList() {
        servers = new HashMap<>();
    }

    public void addServer(String serverId, LocalServer ls){
        servers.put(serverId, ls);
    }

    @Nullable
    public LocalServer getServer(String id){
        if(servers.containsKey(id)) return servers.get(id);
        else return null;
    }

    public boolean removeServer(String id){
        if(servers.containsKey(id)){
            servers.remove(id);
            return true;
        }
        else return false;
    }

    public List<LocalServer> getServerList(){
        return new ArrayList<>(servers.values());
    }

    public Map<String, LocalServer> getMap(){
        return servers;
    }

}
