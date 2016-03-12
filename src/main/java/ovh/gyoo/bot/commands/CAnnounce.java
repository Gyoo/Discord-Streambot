package ovh.gyoo.bot.commands;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.LocalServer;
import ovh.gyoo.bot.data.MessageItem;
import ovh.gyoo.bot.data.ServerList;

public class CAnnounce implements Command{

    public static String name = "announce";
    private static String description = "`announce` : Hidden command, shush!";

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        if(isAllowed("", e.getAuthor().getId())){
            for(LocalServer ls : ServerList.getInstance().getServerList()){
                MessageItem message = new MessageItem(ls.getId(), MessageItem.Type.GUILD, new MessageBuilder().appendString(" " + content).build());
                DiscordInstance.getInstance().addToQueue(message);
            }
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
