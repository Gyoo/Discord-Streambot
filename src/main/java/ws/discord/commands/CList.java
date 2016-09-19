package ws.discord.commands;

import dao.Dao;
import entity.*;
import entity.local.MessageCreateAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CList extends Command{

    public static String name = "list";

    public CList(JDA jda, Dao dao) {
        super(jda, dao);
        description = "`list <option>` : List data from the bot";
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message;
        MessageBuilder builder = new MessageBuilder();
        GuildEntity guild = dao.getLongId(GuildEntity.class, e.getGuild().getId());
        List<String> list = new ArrayList<>();
        switch (content){
            case "game":
                list.addAll(guild.getGames().stream().map(GameEntity::getName).collect(Collectors.toList()));
                break;
            case "channel":
                list.addAll(guild.getChannels().stream().map(ChannelEntity::getName).collect(Collectors.toList()));
                break;
            case "tag":
                list.addAll(guild.getTags().stream().map(TagEntity::getName).collect(Collectors.toList()));
                break;
            case "manager":
                for(ManagerEntity managerEntity : guild.getManagers()){
                    String name = jda.getUserById(Long.toString(managerEntity.getUserId())).getUsername();
                    list.add(name);
                }
                break;
            case "team":
                list.addAll(guild.getTeams().stream().map(TeamEntity::getName).collect(Collectors.toList()));
                break;
            case "permissions":
                for(PermissionEntity permissionEntity : guild.getPermissions()){
                    String role;
                    if(permissionEntity.getRoleId() == 0) role = "Everyone";
                    else role = jda.getGuildById(e.getGuild().getId()).getRoleById(Long.toString(permissionEntity.getRoleId())).getName();
                    String typeString;
                    switch(permissionEntity.getLevel()){
                        case 0:
                            typeString = "USE";
                            break;
                        case 1:
                            typeString = "QUEUE";
                            break;
                        case 2:
                            typeString = "FORBID";
                            break;
                        default:
                            typeString = "UNKNOWN";
                    }
                    list.add(role + ": " + permissionEntity.getCommand().getName() + " -> " + typeString);
                }
                break;
            default:
                MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, new MessageBuilder()
                        .appendString("Unknown option : " + content)
                        .build());
                return;
        }
        builder.appendString("List of " + content + (content.endsWith("s") ? "":"s") + " for server **" + e.getGuild().getName() + "**\n");
        for(String s : list){
            builder.appendString(s + "\n");
        }
        message = builder.build();
        MessageHandler.getInstance().addCreateToQueue(e.getAuthor().getPrivateChannel().getId(), MessageCreateAction.Type.PRIVATE, message);
    }
}
