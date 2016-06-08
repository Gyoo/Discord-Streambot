package ws.discord.commands;

import dao.Dao;
import entity.GuildEntity;
import entity.local.MessageCreateAction;
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
            builder.appendString("Server count : " + dao.count(GuildEntity.class));
            message = builder.build();
            MessageHandler.getInstance().addCreateToQueue(e.getAuthor().getPrivateChannel().getId(), MessageCreateAction.Type.PRIVATE, message);
        }
    }
}
