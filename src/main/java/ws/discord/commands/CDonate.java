package ws.discord.commands;

import dao.Dao;
import entity.local.MessageCreateAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;

public class CDonate extends Command{

    public static String name = "donate";

    public CDonate(JDA jda, Dao dao) {
        super(jda, dao);
        description = "`donate` : If you like my work, please consider making a donation :)";
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message = new MessageBuilder()
                .appendString("Please consider making a donation if you like the bot! http://bit.ly/StreambotDonate")
                .build();
        MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, message);
    }
}
