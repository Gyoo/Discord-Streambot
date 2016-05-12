package ws.discord.commands;

import dao.Dao;
import entity.GuildEntity;
import entity.local.MessageItem;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;


public class CServers extends Command{

    public static String name = "servers";

    public CServers(JDA jda, Dao dao) {
        super(jda, dao);
        description = "`servers` : Hidden command, shush!";
        allows.add(Allowances.ADMIN);
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        if(isAllowed("", e.getAuthor().getId(), allows, 0)){
            Message message;
            MessageBuilder builder = new MessageBuilder();
            builder.appendString("Server count : " + dao.count(GuildEntity.class) + "\n");
            message = builder.build();
            MessageHandler.getInstance().addToQueue(e.getAuthor().getPrivateChannel().getId(), MessageItem.Type.PRIVATE, message);
        }
    }
}
