package ovh.gyoo.bot.data;

import net.dv8tion.jda.entities.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalServer{

    String serverID;
    String id;
    List<String> gameList;
    List<String> userList;
    List<String> tagList;
    List<String> managers;
    Map<String, Permissions> permissionsMap = new HashMap<>();
    List<QueueItem> commandsQueue = new ArrayList<>();
    boolean active;

    public LocalServer(String id, String serverID){
        this.id = id;
        gameList = new ArrayList<>();
        userList = new ArrayList<>();
        tagList = new ArrayList<>();
        managers = new ArrayList<>();
        active = false;
        this.serverID = serverID;
        initPermissions();
    }

    public void initPermissions(){
        Permissions p = new Permissions();
        p.addPermission("everyone", Permissions.FORBID);
        addPermission("add", p);
        addPermission("remove", p);
    }

    public List<String> getGameList() {
        return gameList;
    }

    public List<String> getUserList() {
        return userList;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public List<String> getManagers() {
        return managers;
    }

    public String getId() {
        return id;
    }

    public boolean addGame(String game){
        if(gameList.contains(game)) return false;
        else {
            gameList.add(game);
            return true;
        }
    }

    public boolean removeGame(String game){
        if(gameList.contains(game)){
            gameList.remove(game);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean addUser(String user){
        if(userList.contains(user)) return false;
        else {
            userList.add(user);
            return true;
        }
    }

    public boolean removeUser(String user){
        if(userList.contains(user)){
            userList.remove(user);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean addTag(String tag){
        if(tagList.contains(tag)) return false;
        else {
            tagList.add(tag);
            return true;
        }
    }

    public boolean removeTag(String tag){
        if(tagList.contains(tag)){
            tagList.remove(tag);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean addManager(String user){
        if(managers.contains(user)) return false;
        else {
            managers.add(user);
            return true;
        }
    }

    public boolean removeManager(String user){
        if(managers.contains(user)){
            managers.remove(user);
            return true;
        }
        else {
            return false;
        }
    }

    public String getServerID() {
        return serverID;
    }
    public void setId(String channelID){id = channelID;}

    public void activate(){active = true;}
    public void disactivate(){active = false;}

    public boolean isActive(){return active;}

    public Map<String, Permissions> getPermissionsMap() {
        return permissionsMap;
    }

    public void addPermission(String command, Permissions p){
        permissionsMap.put(command, p);
    }

    public void queueCommand(String author, String s){
        commandsQueue.add(new QueueItem(author, s));
    }

    public List<QueueItem> getCommandsQueue() {
        return commandsQueue;
    }

    public void clearQueue(){
        commandsQueue.clear();
    }
}
