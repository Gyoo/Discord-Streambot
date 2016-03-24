package ovh.gyoo.bot.commands;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ovh.gyoo.bot.data.*;
import ovh.gyoo.bot.writer.Logger;

public class CPermissions implements Command{

    public static String name = "permissions";
    private static String description = "`permissions <use|queue|forbid> <command> <role|everyone>` : Gives permission level for a command to the users of a role (supports `add` and `remove` commands only currently)";

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        MessageItem message = new MessageItem(e.getTextChannel().getId(), MessageItem.Type.GUILD);
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId()))
            message.setMessage(new MessageBuilder().appendString("You are not allowed to use this command").build());
        else{
            String[] params = content.split(" ");
            if(params.length != 3){
                message.setMessage(new MessageBuilder()
                        .appendString("Error : wrong number of parameters")
                        .build());
                DiscordInstance.getInstance().addToQueue(message);
                return;
            }
            /* params[0] = "use" || "queue" || "forbid"
             * params[1] = <command>
             * params[2] = <role> || "everyone"
             */
            // Type of permission
            int type;
            switch (params[0]){
                case "use":
                    type = Permissions.USE;
                    break;
                case "queue":
                    type = Permissions.QUEUE;
                    break;
                case "forbid":
                    type = Permissions.FORBID;
                    break;
                default:
                    message.setMessage(new MessageBuilder()
                            .appendString("Unknown parameter : " + params[0])
                            .build());
                    DiscordInstance.getInstance().addToQueue(message);
                    return;
            }
            // Command affected
            String command;
            switch (params[1]){
                case "add":
                case "remove":
                    command = params[1]; // Kinda overkill but I like to know what I'm manipulating, hence the variable rename
                    break;
                default:
                    message.setMessage(new MessageBuilder()
                            .appendString("Command unknown or unsupported : " + params[1])
                            .build());
                    DiscordInstance.getInstance().addToQueue(message);
                    return;
            }
            // Role affected
            String role = "";
            if(params[2].equals("everyone")) role = params[2];
            else{
                for(Role r : e.getGuild().getRoles()){
                    if(r.getName().equals(params[2])){
                        role = r.getId();
                        break;
                    }
                }
            }
            if(role.equals("")){
                message.setMessage(new MessageBuilder()
                        .appendString("Unknown role : " + params[2])
                        .build());
                DiscordInstance.getInstance().addToQueue(message);
                return;
            }
            Permissions p = ServerList.getInstance().getServer(e.getGuild().getId()).getPermissionsMap().get(command);
            p.addPermission(role, type);
            ServerList.getInstance().getServer(e.getGuild().getId()).addPermission(command, p);
            message.setMessage(new MessageBuilder()
                    .appendString("Added permission for Role " + params[2] + "!")
                    .build());
        }
        DiscordInstance.getInstance().addToQueue(message);
    }

    @Override
    public String getDescription(){
        return description;
    }

    @Override
    public boolean isAllowed(String serverID, String authorID) {
        try{
            return ServerList.getInstance().getServer(serverID).getManagers().contains(authorID);
        } catch(NullPointerException e){
            String message = "Guild ID : " + serverID + "\n" + "Guild Name : " + DiscordInstance.getInstance().getDiscord().getGuildById(serverID).getName();
            Logger.writeToErr(e, message);
            return false;
        }
    }
}

