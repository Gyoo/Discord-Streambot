package ws.discord.commands;

import dao.Dao;
import entity.GuildEntity;
import entity.QueueitemEntity;
import entity.local.MessageCreateAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;

public class CQueue extends Command{

    public static String name = "queue";

    public CQueue(JDA jda, Dao dao) {
        super(jda, dao);
        description = "`queue` : shows queued commands by users with the QUEUE permission set. **Warning** : Using this command cleans the queue !";
        allows.add(Allowances.MANAGERS);
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message;
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId(), allows, 0, null))
            message = new MessageBuilder().appendString("You are not allowed to use this command").build();
        else{
            GuildEntity guildEntity = dao.getLongId(GuildEntity.class, e.getGuild().getId());
            MessageBuilder builder = new MessageBuilder();
            builder.appendString("Commands queue for server " + e.getGuild().getName() + "\n");
            for(QueueitemEntity q : guildEntity.getQueue()){
                String username = jda.getUserById(Long.toString(q.getUserId())).getUsername();
                builder.appendString(username + " : " + q.getCommand() + "\n");
                dao.deleteIntId(QueueitemEntity.class, q.getID());
            }
            message = builder.build();
        }
        MessageHandler.getInstance().addCreateToQueue(e.getAuthor().getPrivateChannel().getId(), MessageCreateAction.Type.PRIVATE, message);
    }
}