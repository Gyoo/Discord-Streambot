package ovh.gyoo.bot.commands;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.MessageItem;
import ovh.gyoo.bot.data.OnlineMap;

public class CStreams implements Command {

    public static String name = "streams";
    private static String description = "`streams` : List of online streams";

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        MessageItem message = new MessageItem(e.getAuthor().getPrivateChannel().getId(), MessageItem.Type.PRIVATE);
        if(!OnlineMap.getInstance().getMap().containsKey(e.getGuild().getId())){
            OnlineMap.getInstance().addServer(e.getGuild().getId());
            message.setMessage(new MessageBuilder()
                    .appendString("No streams online ! :(")
                    .build());
        }
        else{
            if(OnlineMap.getInstance().getNameList(e.getGuild().getId()).size() == 0) message.setMessage(new MessageBuilder()
                    .appendString("No streams online ! :(")
                    .build());
            MessageBuilder builder = new MessageBuilder();
            for(String name : OnlineMap.getInstance().getNameList(e.getGuild().getId())){
                builder.appendString("http://twitch.tv/" + name + "\n");
            }
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
        return true;
    }
}
