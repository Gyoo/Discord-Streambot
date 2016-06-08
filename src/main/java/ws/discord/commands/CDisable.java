package ws.discord.commands;

import dao.Dao;
import entity.GuildEntity;
import entity.local.MessageCreateAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;

public class CDisable extends Command{

    public static String name = "disable";

    public CDisable(JDA jda, Dao dao) {
        super(jda, dao);
        allows.add(Allowances.MANAGERS);
        description = "`disable` : Disables bot";
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message;
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId(), allows, 1))
            message = new MessageBuilder().appendString("You are not allowed to use this command").build();
        else{
            GuildEntity guild = dao.getLongId(GuildEntity.class, e.getGuild().getId());
            guild.setActive(false);
            dao.saveOrUpdate(guild);
            message = new MessageBuilder().appendString("Bot disabled !").build();
        }
        MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD,message);
    }

}
