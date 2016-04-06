package ovh.gyoo.bot.commands;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.LocalServer;
import ovh.gyoo.bot.data.MessageItem;
import ovh.gyoo.bot.data.ServerList;
import ovh.gyoo.bot.writer.Logger;

public class CCompact implements Command{

    public static String name = "compact";
    private static String description = "`compact on|off` : Enables/Disables compact mode";

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        MessageItem message = new MessageItem(e.getTextChannel().getId(), MessageItem.Type.GUILD);
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId()))
            message.setMessage(new MessageBuilder().appendString("You are not allowed to use this command").build());
        else {
            String[] params = content.toLowerCase().split(" ");
            if(params.length == 0) {
                message.setMessage(new MessageBuilder().appendString("Please set the value to `on` or `off`").build());
            }
            else{
                switch(params[0]){
                    case "on":
                        ServerList.getInstance().getServer(e.getGuild().getId()).setCompact(true);
                        message.setMessage(new MessageBuilder().appendString("Compact mode enabled !").build());
                        break;
                    case "off":
                        ServerList.getInstance().getServer(e.getGuild().getId()).setCompact(false);
                        message.setMessage(new MessageBuilder().appendString("Compact mode disabled !").build());
                        break;
                    default:
                        message.setMessage(new MessageBuilder().appendString("Please set the value to `on` or `off`").build());
                        break;
                }
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
        try{
            return ServerList.getInstance().getServer(serverID).getManagers().contains(authorID);
        } catch(NullPointerException e){
            String message = "Guild ID : " + serverID + "\n" + "Guild Name : " + DiscordInstance.getInstance().getDiscord().getGuildById(serverID).getName();
            Logger.writeToErr(e, message);
            return false;
        }
    }
}

