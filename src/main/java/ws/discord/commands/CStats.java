package ws.discord.commands;

import dao.Dao;
import entity.GuildEntity;
import entity.local.MessageCreateAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.time.DurationFormatUtils;
import ws.discord.messages.MessageHandler;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.time.Period;


public class CStats extends Command{

    public static String name = "stats";

    public CStats(JDA jda, Dao dao) {
        super(jda, dao);
        description = "`stats` : Hidden command, shush!";
        allows.add(Allowances.ADMIN);
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        if(isAllowed("", e.getAuthor().getId(), allows, 0, null)){
            Message message;
            MessageBuilder builder = new MessageBuilder();
            builder.appendString("Server count : " + dao.count(GuildEntity.class) + "\n");
            builder.appendString("Users reached : " + jda.getUsers().size() + "\n");
            RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
            builder.appendString("Uptime : " + DurationFormatUtils.formatDuration(rb.getUptime(), "ddd 'days', HH:mm:ss"));
            message = builder.build();
            MessageHandler.getInstance().addCreateToQueue(e.getAuthor().getPrivateChannel().getId(), MessageCreateAction.Type.PRIVATE, message);
        }
    }
}
