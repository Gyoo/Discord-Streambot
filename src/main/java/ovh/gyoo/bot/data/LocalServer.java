package ovh.gyoo.bot.data;

import com.mb3364.http.RequestParams;
import com.mb3364.twitch.api.handlers.StreamsResponseHandler;
import com.mb3364.twitch.api.models.Stream;
import com.mb3364.twitch.api.models.Team;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import ovh.gyoo.bot.handlers.TeamInfoRequestHandler;
import ovh.gyoo.bot.handlers.TeamRequestHandler;
import ovh.gyoo.bot.handlers.TeamUtils;
import ovh.gyoo.bot.handlers.TwitchChecker;

import java.util.*;

public class LocalServer{

    String serverID;
    String id;
    List<String> gameList;
    List<String> userList;
    List<String> tagList;
    List<String> managers;
    List<String> teamList;
    Map<String, Permissions> permissionsMap = new HashMap<>();
    Map<String, Boolean> notifs = new HashMap<>();
    List<QueueItem> commandsQueue = new ArrayList<>();
    boolean active;
    boolean compact;

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
        initPermissions();
        initNotifs();
    }

    public void initPermissions(){
        Permissions p = new Permissions();
        p.addPermission("everyone", Permissions.FORBID);
        addPermission("add", p);
        p = new Permissions();
        p.addPermission("everyone", Permissions.FORBID);
        addPermission("remove", p);
    }

    public void initNotifs(){
        notifs.put("everyone", false);
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

    public List<String> getPermissionsList(){
        JDA api = DiscordInstance.getInstance().getDiscord();
        List<String> list = new ArrayList<>();
        for(Map.Entry<String, Permissions> entry : permissionsMap.entrySet()){
            for(Map.Entry<String, Integer> permsEntry : entry.getValue().getPerms().entrySet()){
                String role = "";
                if(permsEntry.getKey().equals("everyone")) role = "everyone";
                else{
                    for(Role r : api.getGuildById(serverID).getRoles()) {
                        if(permsEntry.getKey().equals(r.getId())) {
                            role = r.getName();
                            break;
                        }
                    }
                }
                list.add(entry.getKey() + ": " + role + " -> " + Permissions.getPermissionLevel(permsEntry.getValue()));
            }
        }
        return list;
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

    public Map<String, Boolean> getNotifs(){ return notifs;}

    public void addNotif(String name, boolean value){
        notifs.put(name, value);
    }

    public void checkStreams(boolean witholdOutput) {
        if (!isActive()) return;
        if (gameList.size() == 0 && userList.size() == 0 && teamList.size() == 0) return;
        if (gameList.size() == 0) addGame("");
        for (String game : gameList) {
            if(userList.size() == 0 && game.equals("")) break;
            RequestParams rp = new RequestParams();
            rp.put("game", game);
            String channels = "";
            // TODO: Check if userList.size > 100, then send multiple reqeusts
            if (userList.size() > 0) {
                for (String name : userList) {
                    channels += "," + name;
                }
                channels = channels.substring(1);
                rp.put("channel", channels);
            }
            rp.put("limit", 100);
            TwitchChecker.getTwitch().streams().get(rp, new StreamsResponseHandler() {
                @Override
                public void onSuccess(int i, List<Stream> list) {
                    for (Stream stream : list) {
                        if(!witholdOutput && !OnlineMap.getInstance().isDisplayed(serverID, stream))  {
                            OnlineMap.getInstance().addToList(serverID, stream);
                            updateDiscordList(stream);
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
        if(teamList.size() > 0) {
            for(String name : teamList) {
                TeamUtils.getTeam(name, new TeamRequestHandler() {
                    @Override
                    public void onSuccess(Team team) {

                        TeamUtils.GetChannels(team, new TeamInfoRequestHandler() {
                            @Override
                            public void onSuccess(List<Stream> streams) {
                                for(Stream stream : streams) {
                                    if(!OnlineMap.getInstance().isDisplayed(serverID, stream)) {
                                        OnlineMap.getInstance().addToList(serverID, stream);
                                        if(!witholdOutput) updateDiscordList(stream);
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

                    @Override
                    public void onFailure(int i, String s, String s1) {

                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });

            }
        }

        if (gameList.contains("")) removeGame("");
    }

    private void updateDiscordList(Stream stream) {
        String linkBeginning = compact ? "<" : "`";
        String linkEnd = compact ? ">" : " `";
        if(streamMatchesAttributes(stream)) {
            MessageBuilder builder = new MessageBuilder();
            for(Iterator<Map.Entry<String, Boolean>> it = getNotifs().entrySet().iterator(); it.hasNext();){
                Map.Entry<String, Boolean> entry = it.next();
                if(entry.getValue()) {
                    if(entry.getKey().equals("everyone")) builder.appendEveryoneMention().appendString(" ");
                    else {
                        User u = DiscordInstance.getInstance().getDiscord().getUserById(entry.getKey());
                        if(null == u) it.remove();
                        else builder.appendMention(u).appendString(" ");
                    }
                }
            }
            builder.appendString("NOW LIVE : " + linkBeginning + "http://twitch.tv/" + stream.getChannel().getName() + linkEnd + " playing " + stream.getGame() + " | " + stream.getChannel().getStatus() + " | (" + stream.getChannel().getBroadcasterLanguage() + ")");
            DiscordInstance.getInstance().addToQueue(new MessageItem(getId(), MessageItem.Type.GUILD, builder.build()));
        }
    }

    /**
     * Checks if stream matches the valid attributes to display a message on the discord server.
     *
     * @param stream The stream to verify
     * @return true if the stream contains one of the tags in this server's tagList
     */
    private boolean streamMatchesAttributes(Stream stream) {
        if (!stream.isOnline()) return false;

        if(gameList.size() != 0) {
            if(!gameList.contains(stream.getChannel().getGame())) return false;
        }

        if (tagList.size() > 0) {
            boolean hasTag = false;
            List<String> split;
            if (null != stream.getChannel().getStatus())
                split = Arrays.asList(stream.getChannel().getStatus().toLowerCase().split(" "));
            else split = new ArrayList<>();
            for (String tag : tagList) {
                for(String word : split){
                    if(word.startsWith(tag)){
                        hasTag = true;
                        break;
                    }
                }
                if(hasTag) break;
            }
            if (!hasTag) return false;
        }

        return true;
    }

}
