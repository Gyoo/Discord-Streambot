package ovh.gyoo.bot.commands;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.MessageItem;
import ovh.gyoo.bot.data.ServerList;
import ovh.gyoo.bot.writer.Logger;

public class CDisable implements Command{

    public static String name = "disable";
    private static String description = "`disable` : Disables bot";

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        MessageItem message = new MessageItem(e.getTextChannel().getId(), MessageItem.Type.GUILD);
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId()))
            message.setMessage(new MessageBuilder().appendString("You are not allowed to use this command").build());
        else{
            ServerList.getInstance().getServer(e.getGuild().getId()).disactivate();
            message.setMessage(new MessageBuilder().appendString("Bot disabled !").build());
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
