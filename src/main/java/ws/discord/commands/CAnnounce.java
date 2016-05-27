package ws.discord.commands;

import dao.Dao;
import entity.GuildEntity;
import entity.local.MessageItem;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;

import java.util.List;

public class CAnnounce extends Command{

    public static String name = "announce";

    public CAnnounce(JDA jda, Dao dao) {
        super(jda, dao);
        allows.add(Allowances.ADMIN);
        description = "`announce` : Hidden command, shush!";
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        if(isAllowed("", e.getAuthor().getId(), allows, 0)){
            List<GuildEntity> guilds = dao.getAll(GuildEntity.class);
            for(GuildEntity guild : guilds){
                Message message = new MessageBuilder().appendString(content).build();
                MessageHandler.getInstance().addToQueue(guild.getChannelId(), MessageItem.Type.GUILD, message);
            }
        }
    }

}
