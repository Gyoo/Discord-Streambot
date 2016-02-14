package ovh.gyoo.bot.commands;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.MessageItem;
import ovh.gyoo.bot.data.ServerList;

public class CToggle implements Command{

    public static String name = "toggle";
    private static String description = "`toggle everyone` : enables/disables the use of `@everyone` in the stream announces";

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        MessageItem message = new MessageItem(e.getTextChannel().getId(), MessageItem.Type.GUILD);
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId()))
            message.setMessage(new MessageBuilder().appendString("You are not allowed to use this command").build());
        else{
            boolean b = !ServerList.getInstance().getServer(e.getGuild().getId()).getToggles().get("everyone"); //Gets the inverted value and put it back in the map
            ServerList.getInstance().getServer(e.getGuild().getId()).addToggle("everyone", b);
            if(b) message.setMessage(new MessageBuilder().appendString("Announces will mention `@everyone` !").build());
            else message.setMessage(new MessageBuilder().appendString("Announces will not mention `@everyone` !").build());
        }
        DiscordInstance.getInstance().addToQueue(message);
    }

    @Override
    public String getDescription(){
        return description;
    }

    @Override
    public boolean isAllowed(String serverID, String authorID) {
        return ServerList.getInstance().getServer(serverID).getManagers().contains(authorID);
    }
}
