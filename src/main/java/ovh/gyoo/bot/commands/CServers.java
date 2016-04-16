package ovh.gyoo.bot.commands;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.LocalServer;
import ovh.gyoo.bot.data.MessageItem;
import ovh.gyoo.bot.data.ServerList;


public class CServers implements Command{

    public static String name = "servers";
    private static String description = "`servers` : Hidden command, shush!";

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        if(isAllowed("", e.getAuthor().getId())){
            MessageItem message = new MessageItem(e.getAuthor().getPrivateChannel().getId(), MessageItem.Type.PRIVATE);
            MessageBuilder builder = new MessageBuilder();
            builder.appendString("Server count : " + ServerList.getInstance().getServerList().size() + "\n");
            message.setMessage(builder.build());
            DiscordInstance.getInstance().addToQueue(message);
            String offline = "\n**Disabled**\n";
            for(LocalServer ls : ServerList.getInstance().getServerList()){
                if(!ls.isActive()){
                    String serverName = DiscordInstance.getInstance().getDiscord().getGuildById(ls.getServerID()).getName();
                    offline += serverName + "\n";
                }
            }
            message = new MessageItem(e.getAuthor().getPrivateChannel().getId(), MessageItem.Type.PRIVATE);
            message.setMessage(new MessageBuilder().appendString(offline).build());
            DiscordInstance.getInstance().addToQueue(message);
        }
    }

    @Override
    public String getDescription(){
        return description;
    }

    @Override
    public boolean isAllowed(String serverID, String authorID) {
        return authorID.equals("63263941735755776");
    }
}
