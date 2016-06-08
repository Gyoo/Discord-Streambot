package ws.discord.commands;

import dao.Dao;
import entity.GuildEntity;
import entity.local.MessageCreateAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;

public class CCompact extends Command{

    public static String name = "compact";

    public CCompact(JDA jda, Dao dao) {
        super(jda, dao);
        allows.add(Allowances.MANAGERS);
        description = "`compact on|off` : Enables/Disables compact mode";
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message;
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId(), allows, 0))
            message = new MessageBuilder().appendString("You are not allowed to use this command").build();
        else {
            String[] params = content.toLowerCase().split(" ");
            if(params.length == 0) {
                message = new MessageBuilder().appendString("Please set the value to `on` or `off`").build();
            }
            else{
                GuildEntity guild = dao.getLongId(GuildEntity.class, e.getGuild().getId());
                switch(params[0]){
                    case "on":
                        guild.setCompact(true);
                        message = new MessageBuilder().appendString("Compact mode enabled !").build();
                        break;
                    case "off":
                        guild.setCompact(false);
                        message = new MessageBuilder().appendString("Compact mode disabled !").build();
                        break;
                    default:
                        message = new MessageBuilder().appendString("Please set the value to `on` or `off`").build();
                        break;
                }
                dao.saveOrUpdate(guild);
            }
        }
        MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, message);
    }
}

