package ws.discord.commands;

import dao.Dao;
import entity.GuildEntity;
import entity.NotificationEntity;
import entity.local.MessageCreateAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
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
                MessageBuilder builder = new MessageBuilder();
                for(NotificationEntity notif : guild.getNotifications()){
                    switch(Long.toString(notif.getUserId())){
                        case "0":
                            builder.appendEveryoneMention().appendString(" ");
                            break;
                        case "1":
                            builder.appendString("@here ");
                            break;
                        default:
                            User user = jda.getUserById(Long.toString(notif.getUserId()));
                            builder.appendMention(user).appendString(" ");
                            break;
                    }
                }
                Message message = builder.appendString(content).build();
                MessageHandler.getInstance().addCreateToQueue(guild.getChannelId(), MessageCreateAction.Type.GUILD, message);
            }
        }
    }

}
