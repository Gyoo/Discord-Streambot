package ovh.gyoo.bot.commands;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.MessageItem;
import ovh.gyoo.bot.data.ServerList;
import ovh.gyoo.bot.writer.Logger;

public class CEnable implements Command{

    public static String name = "enable";
    private static String description = "`enable` : Enables bot after configuration";

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        MessageItem message = new MessageItem(e.getTextChannel().getId(), MessageItem.Type.GUILD);
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId()))
            message.setMessage(new MessageBuilder().appendString("You are not allowed to use this command").build());
        else{
            if(hasPermissions(e.getTextChannel().getId())){
                ServerList.getInstance().getServer(e.getGuild().getId()).activate();
                message.setMessage(new MessageBuilder().appendString("Bot enabled !").build());
            }
            else{
                String channelName = DiscordInstance.getInstance().getDiscord().getTextChannelById(ServerList.getInstance().getServer(e.getGuild().getId()).getId()).getName();
                message = new MessageItem(e.getAuthor().getPrivateChannel().getId(), MessageItem.Type.PRIVATE);
                message.setMessage(new MessageBuilder().appendString("Missing permissions on channel " + channelName + " : Bot needs to be able to read and write in this channel. \n"
                        + "Bot is still disabled. Type `!streambot enable` again when the permissions are good to go !").build());
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

    public boolean hasPermissions(String channelID){
        User self = DiscordInstance.getInstance().getDiscord().getUserById(DiscordInstance.getInstance().getDiscord().getSelfInfo().getId());
        TextChannel channel = DiscordInstance.getInstance().getDiscord().getTextChannelById(channelID);
        return channel.checkPermission(self, Permission.MESSAGE_WRITE) && channel.checkPermission(self, Permission.MESSAGE_READ);
    }
}
