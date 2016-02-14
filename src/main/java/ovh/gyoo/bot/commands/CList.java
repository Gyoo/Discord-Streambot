package ovh.gyoo.bot.commands;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ovh.gyoo.bot.data.*;

import java.util.ArrayList;
import java.util.List;

public class CList implements Command{

    public static String name = "list";
    private static String description = "`list <option>` : List data from the bot";

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        MessageItem message = new MessageItem(e.getAuthor().getPrivateChannel().getId(), MessageItem.Type.PRIVATE);
        MessageBuilder builder = new MessageBuilder();
        LocalServer ls = ServerList.getInstance().getServer(e.getGuild().getId());
        List<String> list;
        switch (content){
            case "game":
                list = ls.getGameList();
                break;
            case "channel":
                list = ls.getUserList();
                break;
            case "tag":
                list = ls.getTagList();
                break;
            case "manager":
                list = ls.getManagers();
                break;
            case "permissions":
                list = ls.getPermissionsList();
                break;
            default:
                DiscordInstance.getInstance().addToQueue(new MessageItem(e.getTextChannel().getId(), MessageItem.Type.GUILD, new MessageBuilder()
                        .appendString("Unknown option : " + content)
                        .build()));
                return;
        }
        builder.appendString("List of " + content + (content.endsWith("s") ? "":"s") + " for server **" + e.getGuild().getName() + "**\n");
        for(String s : list){
            if(content.equals("manager")) builder.appendString(DiscordInstance.getInstance().getDiscord().getUserById(s).getUsername() + " | ");
            else builder.appendString(s + "\n");
        }
        message.setMessage(builder.build());
        DiscordInstance.getInstance().addToQueue(message);
    }

    @Override
    public String getDescription(){
        return description;
    }

    @Override
    public boolean isAllowed(String serverID, String authorID) {
        return true;
    }
}
