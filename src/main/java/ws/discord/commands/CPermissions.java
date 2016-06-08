package ws.discord.commands;

import dao.Dao;
import entity.CommandEntity;
import entity.GuildEntity;
import entity.PermissionEntity;
import entity.local.MessageCreateAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;

public class CPermissions extends Command{

    public static String name = "permissions";

    public CPermissions(JDA jda, Dao dao) {
        super(jda, dao);
        description = "`permissions <use|queue|forbid> <command> <role|everyone>` : Gives permission level for a command to the users of a role (supports `add` and `remove` commands only currently)";
        allows.add(Allowances.MANAGERS);
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message;
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId(), allows, 0))
            message = new MessageBuilder().appendString("You are not allowed to use this command").build();
        else{
            String[] params = content.split(" ");
            if(params.length != 3){
                message = new MessageBuilder()
                        .appendString("Error : wrong number of parameters")
                        .build();
                MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, message);
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
                    type = 0;
                    break;
                case "queue":
                    type = 1;
                    break;
                case "forbid":
                    type = 2;
                    break;
                default:
                    message = new MessageBuilder()
                            .appendString("Unknown parameter : " + params[0])
                            .build();
                    MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, message);
                    return;
            }
            // Command affected
            Long command;
            switch (params[1]){
                case "add":
                    command = 1L;
                    break;
                case "remove":
                    command = 2L;
                    break;
                default:
                    message = new MessageBuilder()
                            .appendString("Command unknown or unsupported : " + params[1])
                            .build();
                    MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, message);
                    return;
            }
            // Role affected
            Long role = -1L;
            if(params[2].equals("everyone")) role = 0L;
            else{
                for(Role r : e.getGuild().getRoles()){
                    if(r.getName().equals(params[2])){
                        role = Long.parseLong(r.getId());
                        break;
                    }
                }
            }
            if(role == -1L){
                message = new MessageBuilder()
                        .appendString("Unknown role : " + params[2])
                        .build();
                MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, message);
                return;
            }
            GuildEntity guildEntity = dao.getLongId(GuildEntity.class, e.getGuild().getId());
            PermissionEntity permissionEntity = new PermissionEntity();
            permissionEntity.setGuild(guildEntity);
            CommandEntity commandEntity = dao.getLongId(CommandEntity.class, command);
            permissionEntity.setCommand(commandEntity);
            permissionEntity.setRoleId(role);
            permissionEntity.setLevel(type);
            dao.saveOrUpdate(permissionEntity);
            message = new MessageBuilder()
                    .appendString("Added permission for Role " + params[2] + "!")
                    .build();
        }
        MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, message);
    }
}

