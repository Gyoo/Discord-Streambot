package ws.discord.commands;

import common.Logger;
import dao.Dao;
import entity.*;
import entity.local.MessageCreateAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageEmbed;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import ws.discord.messages.MessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CAdd extends Command {

    public static String name = "add";

    private CommandEntity commandEntity;

    public CAdd(JDA jda, Dao dao) {
        super(jda, dao);
        allows.add(Allowances.MANAGERS);
        allows.add(Allowances.PERMISSIONS);
        description = "`add <option> <content>` : Add data to the bot (see options below)";
        commandEntity = dao.getLongId(CommandEntity.class, 1L);
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message;
        GuildEntity guild = dao.getLongId(GuildEntity.class, e.getGuild().getId());
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId(), allows, 1, commandEntity))
            message = new MessageBuilder().appendString("You are not allowed to use this command").build();
        else if(getPermissionsLevel(e.getGuild().getId(), e.getAuthor().getId(), commandEntity) == 1){
            QueueitemEntity queueitemEntity = new QueueitemEntity();
            queueitemEntity.setGuild(guild);
            queueitemEntity.setCommand("`!streambot add " + content + "`");
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
                option = StringUtils.substring(content, 0, content.indexOf(" "));
            } catch(StringIndexOutOfBoundsException sioobe){
                Logger.writeToErr(sioobe, "Content : " + content);
            }
            if(option.isEmpty()) message = new MessageBuilder()
                    .appendString("It looks like an argument is missing. Please make sure you got the command right.")
                    .build();
            else{
                String[] contents = content.substring(content.indexOf(" ")).split("\\|");
                switch (option) {
                    case "game":
                        message = addGame(guild, contents);
                        break;
                    case "channel":
                        message = addChannel(guild, contents);
                        break;
                    case "tag":
                        message = addTag(guild, contents);
                        break;
                    case "manager":
                        message = addManagers(guild, e.getMessage().getMentionedUsers(), e.getAuthor().getId());
                        break;
                    case "team":
                        message = addTeam(guild, contents);
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

    private Message addTeam(GuildEntity guild, String[] team) {
        MessageBuilder mb = new MessageBuilder();
        PlatformEntity platform = dao.getIntId(PlatformEntity.class, 1);
        Set<TeamEntity> teams = guild.getTeams();
        for(String s : team) {
            s = s.trim();
            boolean res = true;
            for(TeamEntity entity : teams){
                if(entity.getName().equalsIgnoreCase(s)){
                    res = false;
                    break;
                }
            }
            if(res) {
                TeamEntity teamEntity = new TeamEntity();
                teamEntity.setGuild(guild);
                teamEntity.setPlatform(platform);
                teamEntity.setName(s);
                teams.add(teamEntity);
                dao.saveOrUpdate(teamEntity);
                mb.appendString("Team " + s + " added to the teams list\n");
            }
            else
                mb.appendString("Team " + s + " is already in the teams list\n");
        }
        return mb.build();
    }

    private Message addGame(GuildEntity guild, String[] game){
        MessageBuilder mb = new MessageBuilder();
        PlatformEntity platform = dao.getIntId(PlatformEntity.class, 1);
        Set<GameEntity> games = guild.getGames();
        for(String s : game) {
            s = s.trim();
            boolean res = true;
            for(GameEntity entity : games){
                if(entity.getName().equalsIgnoreCase(s)){
                    res = false;
                    break;
                }
            }
            if(res) {
                GameEntity gameEntity = new GameEntity();
                gameEntity.setGuild(guild);
                gameEntity.setPlatform(platform);
                gameEntity.setName(s);
                games.add(gameEntity);
                dao.saveOrUpdate(gameEntity);
                mb.appendString("Game " + s + " added to the game list\n");
            }
            else
                mb.appendString("Game " + s + " is already in the game list\n");
        }
        return mb.build();
    }

    private Message addChannel(GuildEntity guild, String[] channel){
        MessageBuilder mb = new MessageBuilder();
        PlatformEntity platform = dao.getIntId(PlatformEntity.class, 1);
        Set<ChannelEntity> channels = guild.getChannels();
        for(String s : channel) {
            s = s.trim();
            boolean res = true;
            for(ChannelEntity entity : channels){
                if(entity.getName().equalsIgnoreCase(s)){
                    res = false;
                    break;
                }
            }
            if(res) {
                ChannelEntity channelEntity = new ChannelEntity();
                channelEntity.setGuild(guild);
                channelEntity.setPlatform(platform);
                channelEntity.setName(s);
                channels.add(channelEntity);
                dao.saveOrUpdate(channelEntity);
                mb.appendString("Channel " + s + " added to the channels list\n");
            }
            else
                mb.appendString("Channel " + s + " is already in the channels list\n");
        }
        return mb.build();
    }

    private Message addTag(GuildEntity guild, String[] tag){
        MessageBuilder mb = new MessageBuilder();
        Set<TagEntity> tags = guild.getTags();
        for(String s : tag) {
            s = s.trim();
            boolean res = true;
            for(TagEntity entity : tags){
                if(entity.getName().equalsIgnoreCase(s)){
                    res = false;
                    break;
                }
            }
            if(res) {
                TagEntity tagEntity = new TagEntity();
                tagEntity.setGuild(guild);
                tagEntity.setName(s);
                tags.add(tagEntity);
                dao.saveOrUpdate(tagEntity);
                mb.appendString("Tag " + s + " added to the tags list\n");
            }
            else
                mb.appendString("Tag " + s + " is already in the tags list\n");
        }
        return mb.build();
    }

    private Message addManagers(GuildEntity guild, List<User> users, String authorID){
        MessageBuilder mb = new MessageBuilder();
        List<Allowances> managerAllowance = new ArrayList<>();
        managerAllowance.add(Allowances.MANAGERS);
        if(isAllowed(Long.toString(guild.getServerId()),authorID,managerAllowance,0, commandEntity)){
            if(users.isEmpty()) {
                mb.appendString("No users detected. Make sure you use the @ mention when adding managers");
            }
            else {
                Set<ManagerEntity> managers = guild.getManagers();
                for(User u : users){
                    boolean res = true;
                    for(ManagerEntity entity : managers){
                        if(entity.getUserId() == Long.parseLong(u.getId())){
                            res = false;
                            break;
                        }
                    }
                    if(res) {
                        ManagerEntity managerEntity = new ManagerEntity();
                        managerEntity.setUserId(Long.parseLong(u.getId()));
                        managerEntity.setGuild(guild);
                        managers.add(managerEntity);
                        dao.saveOrUpdate(managerEntity);
                        mb.appendString("User " + u.getUsername() + " added to the managers list\n");
                    }
                    else
                        mb.appendString("User " + u.getUsername() + " is already in the managers list\n");
                }
            }
        }
        else mb.appendString("You are not allowed to use this command");
        return mb.build();
    }

    @Override
    public CommandEntity getCommandEntity(){
        return commandEntity;
    }
}
