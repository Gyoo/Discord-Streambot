package old;

import com.mb3364.http.RequestParams;
import com.mb3364.twitch.api.handlers.StreamsResponseHandler;
import com.mb3364.twitch.api.models.Stream;
import com.mb3364.twitch.api.models.Team;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import old.Permissions;

import java.util.*;

public class LocalServer{

    private String serverID;
    private String id;
    private List<String> gameList;
    private List<String> userList;
    private List<String> tagList;
    private List<String> managers;
    private List<String> teamList;
    private Map<String, Permissions> permissionsMap = new HashMap<>();
    private Map<String, Boolean> notifs = new HashMap<>();
    private boolean active;
    private boolean compact;

    public LocalServer(String id, String serverID){
        this.id = id;
        gameList = new ArrayList<>();
        userList = new ArrayList<>();
        tagList = new ArrayList<>();
        teamList = new ArrayList<>();
        managers = new ArrayList<>();
        active = false;
        this.serverID = serverID;
        compact = false;
        initNotifs();
    }

    private void initNotifs(){
        notifs.put("everyone", false);
        notifs.put("here", false);
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

    public List<String> getTeamList() { return teamList; }

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

    public boolean addTeam(String team) {
        if(teamList.contains(team.toLowerCase())) return false;
        else {
            teamList.add(team.toLowerCase());
            return true;
        }
    }

    public boolean removeTeam(String team) {
        if(teamList.contains(team.toLowerCase())) {
            teamList.remove(team.toLowerCase());
            return true;
        }
        return false;
    }

    public String getServerID() {
        return serverID;
    }
    public void setId(String channelID){id = channelID;}

    public void activate(){active = true;}
    public void disactivate(){active = false;}

    public boolean isActive(){return active;}

    public boolean isCompact() {
        return compact;
    }

    public void setCompact(boolean compact) {
        this.compact = compact;
    }

    public Map<String, Permissions> getPermissionsMap() {
        return permissionsMap;
    }

    public void addPermission(String command, Permissions p){
        permissionsMap.put(command, p);
    }
}
