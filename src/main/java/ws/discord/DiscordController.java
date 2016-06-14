package ws.discord;

import common.Logger;
import common.util.HibernateUtil;
import dao.Dao;
import entity.GuildEntity;
import entity.ManagerEntity;
import entity.local.MessageCreateAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.InviteUtil;
import ws.discord.commands.*;
import ws.discord.messages.MessageHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscordController extends ListenerAdapter {
    private JDA jda;
    private Dao dao;
    private List<String> options = new ArrayList<>();
    private Map<String, Command> commandMap = new HashMap<>();

    public DiscordController(Dao dao) {
        this.dao = dao;
        options.add("`game` : Game name based on Twitch's list (must be the exact name to work !)");
        options.add("`team` : Twitch team name (ID as mentioned in the team link)");
        options.add("`channel` : Twitch channel name");
        options.add("`tag` : Word or group of words that must be present in the stream's title");
        options.add("`manager` : Discord user (must use the @ alias when using this option !)");
    }

    public void setJda(JDA jda){
        this.jda = jda;
        commandMap.clear();

        commandMap.put(CStreams.name, new CStreams(this.jda, this.dao));
        commandMap.put(CAdd.name, new CAdd(this.jda, this.dao));
        commandMap.put(CRemove.name, new CRemove(this.jda, this.dao));
        commandMap.put(CPermissions.name, new CPermissions(this.jda, this.dao));
        commandMap.put(CNotify.name, new CNotify(this.jda, this.dao));
        commandMap.put(CList.name, new CList(this.jda, this.dao));
        commandMap.put(CEnable.name, new CEnable(this.jda, this.dao));
        commandMap.put(CDisable.name, new CDisable(this.jda, this.dao));
        commandMap.put(CInvite.name, new CInvite(this.jda, this.dao));
        commandMap.put(CMove.name, new CMove(this.jda, this.dao));
        commandMap.put(CCompact.name, new CCompact(this.jda, this.dao));
        commandMap.put(CCleanup.name, new CCleanup(this.jda, this.dao));
        commandMap.put(CQueue.name, new CQueue(this.jda, this.dao));
        commandMap.put(CDonate.name, new CDonate(this.jda, this.dao));
        commandMap.put(CServers.name, new CServers(this.jda, this.dao));
        commandMap.put(CAnnounce.name, new CAnnounce(this.jda, this.dao));
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if(e.isPrivate()){
            if (e.getMessage().getContent().equals("!streambot servers")) {
                commandMap.get("servers").execute(e, "");
            }
            if (e.getMessage().getContent().startsWith("!streambot announce")) {
                commandMap.get("announce").execute(e, e.getMessage().getContent().substring(20));
            }
        }
        else{
            if (e.getMessage().getContent().startsWith("!ping")) {
                MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, new MessageBuilder()
                        .appendString("Pong !")
                        .build());
            }
            if (e.getMessage().getContent().startsWith("!streambot")) {
                try {
                    commands(e, e.getMessage().getContent().substring(11));
                } catch (StringIndexOutOfBoundsException sioobe) {
                    MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, new MessageBuilder()
                            .appendString("You must put a command behind `!streambot` !")
                            .build());
                }
            }
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent e) {
        GuildEntity guildEntity = dao.getLongId(GuildEntity.class, e.getGuild().getId());
        if (guildEntity == null) {
            guildEntity = new GuildEntity();
            guildEntity.setServerId(Long.parseLong(e.getGuild().getId()));
            guildEntity.setChannelId(Long.parseLong(e.getGuild().getPublicChannel().getId()));
            guildEntity.setActive(false);
            guildEntity.setCompact(false);
            dao.saveOrUpdate(guildEntity);

            //Add server owner + Administrators as managers
            ManagerEntity managerEntity = new ManagerEntity();
            managerEntity.setGuild(guildEntity);
            managerEntity.setUserId(Long.parseLong(e.getGuild().getOwnerId()));
            dao.saveOrUpdate(managerEntity);
            for(Role role : e.getGuild().getRoles()){
                if(role.hasPermission(Permission.MANAGE_SERVER)){
                    for(User user : e.getGuild().getUsersWithRole(role)){
                        managerEntity = new ManagerEntity();
                        managerEntity.setGuild(guildEntity);
                        managerEntity.setUserId(Long.parseLong(user.getId()));
                        dao.saveOrUpdate(managerEntity);
                    }
                    break;
                }
            }
            MessageHandler.getInstance().addCreateToQueue(e.getGuild().getOwner().getPrivateChannel().getId(), MessageCreateAction.Type.PRIVATE, new MessageBuilder()
                    .appendString("Thanks for inviting me ! By joining the following Guild, you can have access to guidelines to configure me properly, in the #faq channel : " +
                            InviteUtil.createInvite(jda.getTextChannelById("131483070464393216")).getUrl() + "\n" +
                            "You can also get news about the updates, alert about bugs or just ask questions !")
                    .build());
        }
        /*
        Condition to avoid the bot from re-posting this message when Discord servers are unavailable
        When the bot is invited to a server, the LocalServer is the following :
        - Inactive server
        - No Tag, Game, Channel or Team
        In this case, the message is shown because it's most likely that the server has just been created in the database. In case it's not, it'll remind people that the bot is there :)
         */
        try {
            HibernateUtil.getSession().refresh(guildEntity);
            if (!guildEntity.isActive() && guildEntity.getGames().isEmpty() && guildEntity.getTags().isEmpty() && guildEntity.getTeams().isEmpty() && guildEntity.getChannels().isEmpty()) {
                MessageBuilder builder = new MessageBuilder().appendString("Hello ! I'm StreamBot ! Type `!streambot commands` to see the available commands ! The managers are :\n");
                for (ManagerEntity manager : guildEntity.getManagers()) {
                    builder.appendMention(jda.getUserById(Long.toString(manager.getUserId()))).appendString("\n");
                }
                MessageHandler.getInstance().addCreateToQueue(guildEntity.getChannelId(), MessageCreateAction.Type.GUILD, builder.build());
            }
        } catch (NullPointerException npe) {
            Logger.writeToErr(npe, "Guild name : " + e.getGuild().getName());
        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e) {
        dao.deleteLongId(GuildEntity.class, Long.parseLong(e.getGuild().getId()));
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        if (e.getGuild().getId().equals("131483070464393216")) {
            MessageBuilder builder = new MessageBuilder();
            builder.appendString("Hello " + e.getUser().getUsername() + "!\n");
            builder.appendString("If you wish to add me to your server, use this link : https://discordapp.com/oauth2/authorize?&client_id=170832003715956746&scope=bot&permissions=224256\n");
            builder.appendString("Then, you can follow the guidelines in #faq to set me up!\n");
            builder.appendString("If you forgot the commands, type `!streambot commands` or `!streambot help` on **your** server.\n");
            builder.appendString("Don't hesitate to ask questions to Gyoo, my creator!\n");
            builder.appendString("Hope you'll enjoy my work!");
            MessageHandler.getInstance().addCreateToQueue(e.getUser().getPrivateChannel().getId(), MessageCreateAction.Type.PRIVATE, builder.build());
        }
    }

    private void commands(MessageReceivedEvent e, String command) {
        String[] split = command.split(" ");
        String content = command.substring(command.indexOf(" ") + 1);
        //TODO Make `help` its own command with a different content
        if (split[0].equals("commands") || split[0].equals("help")) {
            MessageBuilder builder = new MessageBuilder();
            builder.appendString("`!streambot <command>`\n");
            builder.appendString("`commands` || `help` : List of available commands\n");
            commandMap.entrySet().stream().filter(c -> c.getValue().isAllowed(e.getGuild().getId(), e.getAuthor().getId(), c.getValue().allows,1)).forEach(c -> builder.appendString(c.getValue().getDescription() + "\n"));
            builder.appendString("\n*Options* :\n");
            for (String option : options) {
                builder.appendString(option + "\n");
            }
            MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, builder.build());
            return;
        }
        if (commandMap.containsKey(split[0]))
            commandMap.get(split[0]).execute(e, content);
        else
            MessageHandler.getInstance().addCreateToQueue(e.getTextChannel().getId(), MessageCreateAction.Type.GUILD, new MessageBuilder()
                    .appendString("Unknown command")
                    .build());
    }
}