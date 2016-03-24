package ovh.gyoo.bot.commands;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.MessageItem;
import ovh.gyoo.bot.data.ServerList;
import ovh.gyoo.bot.writer.Logger;

public class CNotify implements Command {

    public static String name = "notify";
    private static String description = "`notify <me|everyone>` : Adds a mention at the beginning of the announce. `me` will add a mention to you, `everyone` will add the \"everyone\" mention. (Type command again to remove existing notification)";

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        MessageItem message = new MessageItem(e.getTextChannel().getId(), MessageItem.Type.GUILD);
        boolean b;
        switch (content) {
            case "me":
                b = !ServerList.getInstance().getServer(e.getGuild().getId()).getNotifs().getOrDefault(e.getAuthor().getId(), false); //Gets the inverted value and put it back in the map
                ServerList.getInstance().getServer(e.getGuild().getId()).addNotif(e.getAuthor().getId(), b);
                if (b)
                    message.setMessage(new MessageBuilder().appendString("You will be mentioned in announces !").build());
                else
                    message.setMessage(new MessageBuilder().appendString("You will not be mentioned in announces anymore !").build());
                break;
            case "everyone":
                if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId()))
                    message.setMessage(new MessageBuilder().appendString("You are not allowed to use this command").build());
                else{
                    b = !ServerList.getInstance().getServer(e.getGuild().getId()).getNotifs().get("everyone"); //Gets the inverted value and put it back in the map
                    ServerList.getInstance().getServer(e.getGuild().getId()).addNotif("everyone", b);
                    if (b)
                        message.setMessage(new MessageBuilder().appendString("Announces will mention \"everyone\" !").build());
                    else
                        message.setMessage(new MessageBuilder().appendString("Removed \"everyone\" from announces mentions !").build());
                }
                break;
            default:
                message.setMessage(new MessageBuilder()
                        .appendString("Unknown parameter")
                        .build());
                break;
        }
        DiscordInstance.getInstance().addToQueue(message);
    }

    @Override
    public String getDescription() {
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
