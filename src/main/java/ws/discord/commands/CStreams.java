package ws.discord.commands;

import dao.Dao;
import entity.GuildEntity;
import entity.StreamEntity;
import entity.local.MessageItem;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;

public class CStreams extends Command {

    public static String name = "streams";

    public CStreams(JDA jda, Dao dao) {
        super(jda, dao);
        description = "`streams` : List of online streams";
        allows.add(Allowances.ALL);
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message;
        MessageBuilder builder = new MessageBuilder();
        GuildEntity guildEntity = dao.getLongId(GuildEntity.class, e.getGuild().getId());
        if(guildEntity == null || guildEntity.getStreams().size() == 0){
            builder.appendString("No streams online ! :(");
        }
        else {
            for(StreamEntity stream : guildEntity.getStreams()){
                String url;
                switch(stream.getPlatform().getPlatformId()){
                    case 1:
                        url = "http://twitch.tv/";
                        break;
                    default:
                        url = "http://twitch.tv/";
                        break;
                }
                builder.appendString(stream.getChannelName() + " playing " + stream.getGameName() + " at ` " + url + stream.getChannelName() + " ` : " + stream.getStreamTitle() + "\n");
            }
        }
        message = builder.build();
        MessageHandler.getInstance().addToQueue(e.getAuthor().getPrivateChannel().getId(), MessageItem.Type.PRIVATE, message);
    }
}
