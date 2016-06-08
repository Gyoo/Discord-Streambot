package ws.discord.commands;

import common.Logger;
import dao.Dao;
import entity.*;
import entity.local.MessageCreateAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CRemove extends Command {

    public static String name = "remove";

    public CRemove(JDA jda, Dao dao) {
        super(jda, dao);
        description = "`remove <option> <content>` : Remove data from the bot";
        allows.add(Allowances.MANAGERS);
        allows.add(Allowances.PERMISSIONS);
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message;
        GuildEntity guild = dao.getLongId(GuildEntity.class, e.getGuild().getId());
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId(), allows, 1))
            message = new MessageBuilder().appendString("You are not allowed to use this command").build();
        else if(getPermissionsLevel(e.getGuild().getId(), e.getAuthor().getId()) == 1){
            QueueitemEntity queueitemEntity = new QueueitemEntity();
            queueitemEntity.setGuild(guild);
            queueitemEntity.setCommand("`!streambot remove " + content + "`");
            queueitemEntity.setUserId(Long.parseLong(e.getAuthor().getId()));
            dao.saveOrUpdate(queueitemEntity);

            message = new MessageBuilder()
                    .appendString("Your request will be treated by a manager soon! (type `!streambot list manager` to check the managers list)")
                    .build();
        }
        else if(null == content || content.isEmpty()) message = new MessageBuilder()
                .appendString("Missing option")
                .build();
        else {
            String option = "";
            try{
                option = content.substring(0, content.indexOf(" "));
            } catch(StringIndexOutOfBoundsException sioobe){
                Logger.writeToErr(sioobe, content);
            }
            if(option.isEmpty()) message = new MessageBuilder()
                    .appendString("An error has occured. Please let the bot's manager for this server contact @Gyoo.")
                    .build();
            else{
                String[] contents = content.substring(content.indexOf(" ")).split("\\|");
                switch (option) {
                    case "game":
                        message = removeGame(guild, contents);
                        break;
                    case "channel":
                        message = removeChannel(guild, contents);
                        break;
                    case "tag":
                        message = removeTag(guild, contents);
                        break;
                    case "manager":
                        message = removeManagers(guild, e.getMessage().getMentionedUsers(), e.getAuthor().getId());
                        break;
                    case "team":
                        message = removeTeam(guild, contents);
                        break;
                    default:
                        message = new MessageBuilder()
                                .appendString("Unknown option: " + option)
                                .build();
                        break;
                }
            }
        }
        MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, message);
    }

    private Message removeGame(GuildEntity guild, String[] game){
        MessageBuilder mb = new MessageBuilder();
        boolean deleted;
        for(String s : game){
            Iterator<GameEntity> iterator = guild.getGames().iterator();
            s = s.trim();
            deleted = false;
            while(iterator.hasNext()){
                GameEntity entity = iterator.next();
                if(entity.getName().equals(s)){
                    dao.deleteLongId(entity.getClass(), entity.getId());
                    iterator.remove();
                    mb.appendString("Game " + s + " removed from the games list\n");
                    deleted = true;
                    break;
                }
            }
            if(!deleted) mb.appendString("Game " + s + " is not in the games list\n");
        }
        return mb.build();
    }

    private Message removeChannel(GuildEntity guild, String[] channels){
        MessageBuilder mb = new MessageBuilder();
        boolean deleted;
        for(String s : channels){
            Iterator<ChannelEntity> iterator = guild.getChannels().iterator();
            s = s.trim();
            deleted = false;
            while(iterator.hasNext()){
                ChannelEntity entity = iterator.next();
                if(entity.getName().equals(s)){
                    dao.deleteLongId(entity.getClass(), entity.getId());
                    iterator.remove();
                    mb.appendString("Channel " + s + " removed from the channels list\n");
                    deleted = true;
                    break;
                }
            }
            if(!deleted) mb.appendString("Channel " + s + " is not in the channels list\n");
        }
        return mb.build();
    }

    private Message removeTag(GuildEntity guild, String[] tags){
        MessageBuilder mb = new MessageBuilder();
        boolean deleted;
        for(String s : tags){
            Iterator<TagEntity> iterator = guild.getTags().iterator();
            s = s.trim();
            deleted = false;
            while(iterator.hasNext()){
                TagEntity entity = iterator.next();
                if(entity.getName().equals(s)){
                    dao.deleteLongId(entity.getClass(), entity.getId());
                    iterator.remove();
                    mb.appendString("Tag " + s + " removed from the tags list\n");
                    deleted = true;
                    break;
                }
            }
            if(!deleted) mb.appendString("Tag " + s + " is not in the game list\n");
        }
        return mb.build();
    }

    private Message removeTeam(GuildEntity guild, String[] teams) {
        MessageBuilder mb = new MessageBuilder();
        boolean deleted;
        for(String s : teams){
            Iterator<TeamEntity> iterator = guild.getTeams().iterator();
            s = s.trim();
            deleted = false;
            while(iterator.hasNext()){
                TeamEntity entity = iterator.next();
                if(entity.getName().equals(s)){
                    dao.deleteLongId(entity.getClass(), entity.getId());
                    iterator.remove();
                    mb.appendString("Team " + s + " removed from the teams list\n");
                    deleted = true;
                    break;
                }
            }
            if(!deleted) mb.appendString("Team " + s + " is not in the teams list\n");
        }
        return mb.build();
    }

    private Message removeManagers(GuildEntity guild, List<User> users, String userId){
        List<Allowances> removeManagersAllowance = new ArrayList<>();
        removeManagersAllowance.add(Allowances.MANAGERS);
        MessageBuilder builder = new MessageBuilder();
        if(isAllowed(Long.toString(guild.getServerId()),userId,removeManagersAllowance,0)) {
            if(users.isEmpty()){
                builder.appendString("No users detected. Make sure you use the @ mention when adding managers");
            }
            else{
                boolean deleted;
                for (User u : users) {
                    if (guild.getManagers().size() == 1) {
                        builder.appendString("Cannot remove managers : There must be at least one manager per server\n");
                        break;
                    }
                    if (userId.equals(u.getId())) {
                        builder.appendString("You cannot remove yourself !\n");
                        continue;
                    }
                    Iterator<ManagerEntity> iterator = guild.getManagers().iterator();
                    deleted = false;
                    while(iterator.hasNext()){
                        ManagerEntity entity = iterator.next();
                        if(entity.getUserId() == Long.parseLong(u.getId())){
                            dao.deleteIntId(entity.getClass(), entity.getID());
                            iterator.remove();
                            builder.appendString("User " + u.getUsername() + " removed from the managers list\n");
                            deleted = true;
                            break;
                        }
                    }
                    if(!deleted) builder.appendString("User " + u.getUsername() + " is not in the managers list\n");
                }
            }
        }
        else builder.appendString("You are not allowed to use this command");
        return builder.build();
    }
}
