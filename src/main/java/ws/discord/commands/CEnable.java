package ws.discord.commands;

import dao.Dao;
import entity.GuildEntity;
import entity.local.MessageCreateAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;

public class CEnable extends Command{

    public static String name = "enable";

    public CEnable(JDA jda, Dao dao) {
        super(jda, dao);
        description = "`enable` : Enables bot after configuration";
        allows.add(Allowances.MANAGERS);
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message;
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId(), allows, 0))
            message = new MessageBuilder().appendString("You are not allowed to use this command").build();
        else{
            GuildEntity guild = dao.getLongId(GuildEntity.class, e.getGuild().getId());
            guild.setActive(true);
            dao.saveOrUpdate(guild);
            message = new MessageBuilder().appendString("Bot enabled !").build();
        }
        MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, message);
    }
}
