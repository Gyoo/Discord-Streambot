package ws.discord.commands;

import dao.Dao;
import entity.GuildEntity;
import entity.local.MessageCreateAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;

public class CCleanup extends Command {

    public static String name = "cleanup";

    public CCleanup(JDA jda, Dao dao) {
        super(jda, dao);
        description = "`cleanup <none|edit|delete>` : When a stream goes offline, choose to either leave the message (`none`), `edit` the \"NOW LIVE\" to \"OFFLINE\" or `delete` the announce" ;
        allows.add(Allowances.MANAGERS);
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message;
        GuildEntity guildEntity = dao.getLongId(GuildEntity.class, e.getGuild().getId());
        switch (content) {
            case "none":
                guildEntity.setCleanup(0);
                message = new MessageBuilder().appendString("When a stream goes off, nothing will be done to the announces !").build();
                break;
            case "edit":
                guildEntity.setCleanup(1);
                message = new MessageBuilder().appendString("When a stream goes off, announces will be edited !").build();
                break;
            case "delete":
                guildEntity.setCleanup(2);
                message = new MessageBuilder().appendString("When a stream goes off, announces will be deleted !").build();
                break;
            default:
                message = new MessageBuilder()
                        .appendString("Unknown parameter")
                        .build();
                break;
        }
        dao.saveOrUpdate(guildEntity);
        MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, message);
    }
}
