package ws.discord.commands;

import dao.Dao;
import entity.GuildEntity;
import entity.NotificationEntity;
import entity.local.MessageItem;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;

public class CNotify extends Command {

    public static String name = "notify";

    public CNotify(JDA jda, Dao dao) {
        super(jda, dao);
        description = "`notify <me|everyone>` : Adds a mention at the beginning of the announce. `me` will add a mention to you, `everyone` will add the \"everyone\" mention. (Type command again to remove existing notification)";
        allows.add(Allowances.MANAGERS);
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message;
        NotificationEntity notif = null;
        GuildEntity guildEntity = dao.getLongId(GuildEntity.class, e.getGuild().getId());
        switch (content) {
            case "me":
                for(NotificationEntity notificationEntity : guildEntity.getNotifications()){
                    if(notificationEntity.getUserId() == Long.parseLong(e.getAuthor().getId())){
                        notif = notificationEntity;
                        break;
                    }
                }
                if(notif != null){
                    dao.delete(notif);
                    message = new MessageBuilder().appendString("You will not be mentioned in announces anymore !").build();
                }
                else{
                    notif = new NotificationEntity();
                    notif.setGuild(guildEntity);
                    notif.setUserId(Long.parseLong(e.getAuthor().getId()));
                    dao.saveOrUpdate(notif);
                    message = new MessageBuilder().appendString("You will be mentioned in announces !").build();
                }
                break;
            case "everyone":
                if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId(), allows, 0))
                    message = new MessageBuilder().appendString("You are not allowed to use this command").build();
                else{
                    for(NotificationEntity notificationEntity : guildEntity.getNotifications()){
                        if(notificationEntity.getUserId() == 0L){
                            notif = notificationEntity;
                            break;
                        }
                    }
                    if(notif != null){
                        dao.delete(notif);
                        message = new MessageBuilder().appendString("Removed \"everyone\" from announces mentions !").build();
                    }
                    else{
                        notif = new NotificationEntity();
                        notif.setGuild(guildEntity);
                        notif.setUserId(Long.parseLong(e.getAuthor().getId()));
                        dao.saveOrUpdate(notif);
                        message = new MessageBuilder().appendString("Announces will mention \"everyone\" !").build();
                    }
                }
                break;
            case "here":
                if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId(), allows, 0))
                    message = new MessageBuilder().appendString("You are not allowed to use this command").build();
                else{
                    for(NotificationEntity notificationEntity : guildEntity.getNotifications()){
                        if(notificationEntity.getUserId() == 1L){
                            notif = notificationEntity;
                            break;
                        }
                    }
                    if(notif != null){
                        dao.delete(notif);
                        message = new MessageBuilder().appendString("Removed \"here\" from announces mentions !").build();
                    }
                    else{
                        notif = new NotificationEntity();
                        notif.setGuild(guildEntity);
                        notif.setUserId(Long.parseLong(e.getAuthor().getId()));
                        dao.saveOrUpdate(notif);
                        message = new MessageBuilder().appendString("Announces will mention \"here\" !").build();
                    }
                }
                break;
            default:
                message = new MessageBuilder()
                        .appendString("Unknown parameter")
                        .build();
                break;
        }
        MessageHandler.getInstance().addToQueue(e.getTextChannel().getId(), MessageItem.Type.GUILD, message);
    }
}
