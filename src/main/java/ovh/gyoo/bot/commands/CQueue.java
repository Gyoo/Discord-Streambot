package ovh.gyoo.bot.commands;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ovh.gyoo.bot.data.*;
import ovh.gyoo.bot.writer.Logger;

public class CQueue implements Command{

    public static String name = "queue";
    private static String description = "`queue` : shows queued commands by users with the QUEUE permission set. **Warning** : Using this command cleans the queue !";

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        MessageItem message = new MessageItem(e.getAuthor().getPrivateChannel().getId(), MessageItem.Type.PRIVATE);
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId()))
            message.setMessage(new MessageBuilder().appendString("You are not allowed to use this command").build());
        else{
            MessageBuilder builder = new MessageBuilder();
            builder.appendString("Commands queue for server " + e.getGuild().getName() + "\n");
            for(QueueItem q : ServerList.getInstance().getServer(e.getGuild().getId()).getCommandsQueue()){
                builder.appendString(q.getAuthor() + " : " + q.getCommand() + "\n");
            }
            ServerList.getInstance().getServer(e.getGuild().getId()).clearQueue();
            message.setMessage(builder.build());
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