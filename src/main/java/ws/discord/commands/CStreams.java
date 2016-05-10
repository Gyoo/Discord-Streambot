package ws.discord.commands;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.MessageItem;
import ovh.gyoo.bot.data.OnlineMap;
import ovh.gyoo.bot.data.StreamInfo;

public class CStreams implements Command {

    public static String name = "streams";
    private static String description = "`streams` : List of online streams";

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        MessageItem message = new MessageItem(e.getAuthor().getPrivateChannel().getId(), MessageItem.Type.PRIVATE);
        MessageBuilder builder = new MessageBuilder();
        if(!OnlineMap.getInstance().getMap().containsKey(e.getGuild().getId())){
            OnlineMap.getInstance().addServer(e.getGuild().getId());
            builder.appendString("No streams online ! :(");
        }
        else if(OnlineMap.getInstance().getStreamList(e.getGuild().getId()).size() == 0) {
            builder.appendString("No streams online ! :(");
        }
        else {
            for(StreamInfo stream : OnlineMap.getInstance().getStreamList(e.getGuild().getId())){
                builder.appendString(stream.getName() + " playing " + stream.getGame() + " at ` " + stream.getLink() + " ` : " + stream.getTitle() + "\n");
            }
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
