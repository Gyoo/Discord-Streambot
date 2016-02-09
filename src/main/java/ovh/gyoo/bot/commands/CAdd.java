package ovh.gyoo.bot.commands;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ovh.gyoo.bot.data.*;

import java.util.List;

public class CAdd implements Command{

    public static String name = "add";
    private static String description = "`add <option> <content>` : Add data to the bot (see options below)";

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        MessageItem message = new MessageItem(e.getTextChannel().getId(), MessageItem.Type.GUILD);
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId()))
            message.setMessage(new MessageBuilder().appendString("You are not allowed to use this command").build());
        else if(getPermissionLevel(e.getGuild().getId(), e.getAuthor().getId()) == Permissions.QUEUE){
            ServerList.getInstance().getServer(e.getGuild().getId()).queueCommand(e.getAuthor().getUsername(), "`!streambot add " + content + "`");
            message.setMessage(new MessageBuilder()
                    .appendString("Your request will be treated by a manager soon! (type `!streambot list manager` to check the managers list)")
                    .build());
        }
        else {
            String option = content.substring(0, content.indexOf(" "));
            String[] contents = content.substring(content.indexOf(" ")).split("\\|");
            switch (option) {
                case "game":
                    message.setMessage(addGame(e.getGuild().getId(), contents));
                    break;
                case "channel":
                    message.setMessage(addChannel(e.getGuild().getId(), contents));
                    break;
                case "tag":
                    message.setMessage(addTag(e.getGuild().getId(), contents));
                    break;
                case "manager":
                    message.setMessage(addManagers(e.getGuild().getId(), e.getMessage().getMentionedUsers()));
                    break;
                default:
                    message.setMessage(new MessageBuilder()
                            .appendString("Unknown command")
                            .build());
                    break;
            }
        }
        DiscordInstance.getInstance().addToQueue(message);
    }

    @Override
    public String getDescription(){
        return description;
    }

    @Override
    public boolean isAllowed(String serverID, String authorID) {
        LocalServer ls = ServerList.getInstance().getServer(serverID);
        return ls.getManagers().contains(authorID) || getPermissionLevel(serverID, authorID) < Permissions.FORBID;
    }

    private int getPermissionLevel(String serverID, String authorID){
        int res = Permissions.FORBID;
        LocalServer ls = ServerList.getInstance().getServer(serverID);
        if(ls.getManagers().contains(authorID)) return Permissions.USE;
        for(Role r : DiscordInstance.getInstance().getDiscord().getGuildById(serverID).getRolesForUser(DiscordInstance.getInstance().getDiscord().getUserById(authorID))){
            res = Math.min(res,ls.getPermissionsMap().get(name).getPerms().getOrDefault(r.getId(), Permissions.FORBID));
        }
        res = Math.min(res,ls.getPermissionsMap().get(name).getPerms().getOrDefault("everyone", Permissions.FORBID));
        return res;
    }

    private Message addGame(String serverId, String[] game){
        MessageBuilder mb = new MessageBuilder();
        for(String s : game){
            s = s.trim();
            boolean res = ServerList.getInstance().getServer(serverId).addGame(s);
            if(res)
                mb.appendString("Game " + s + " added to the game list\n");
            else
                mb.appendString("Game " + s + " is already in the game list\n");
        }
        return mb.build();
    }

    private Message addChannel(String serverId, String[] channels){
        MessageBuilder mb = new MessageBuilder();
        for(String s : channels){
            s = s.trim();
            boolean res = ServerList.getInstance().getServer(serverId).addUser(s.replaceAll("`", "").toLowerCase());
            if(res)
                mb.appendString("Channel " + s + " added to the channels list\n");
            else
                mb.appendString("Channel " + s + " is already in the channels list\n");
        }
        return mb.build();
    }

    private Message addTag(String serverId, String[] tags){
        MessageBuilder mb = new MessageBuilder();
        for(String s : tags){
            s = s.trim();
            boolean res = ServerList.getInstance().getServer(serverId).addTag(s);
            if(res)
                mb.appendString("Tag " + s + " added to the tags list\n");
            else
                mb.appendString("Tag " + s + " is already in the tags list\n");
        }
        return mb.build();
    }

    private Message addManagers(String serverId, List<User> users){
        MessageBuilder mb = new MessageBuilder();
        for(User u : users){
            boolean res = ServerList.getInstance().getServer(serverId).addManager(u.getId());
            if(res)
                mb.appendString("User " + u.getUsername() + " added to the managers list\n");
            else
                mb.appendString("User " + u.getUsername() + " is already in the managers list\n");
        }
        return mb.build();
    }
}
