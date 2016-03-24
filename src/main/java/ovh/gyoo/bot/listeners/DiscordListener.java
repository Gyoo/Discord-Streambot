package ovh.gyoo.bot.listeners;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.*;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.InviteUtil;
import ovh.gyoo.bot.commands.*;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.MessageItem;
import ovh.gyoo.bot.data.LocalServer;
import ovh.gyoo.bot.data.ServerList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscordListener extends ListenerAdapter {
    JDA api;
    List<String> options = new ArrayList<>();
    Map<String, Command> commandMap = new HashMap<>();

    public DiscordListener(JDA api){
        this.api = api;

        commandMap.put(CStreams.name, new CStreams());
        commandMap.put(CAdd.name, new CAdd());
        commandMap.put(CRemove.name, new CRemove());
        commandMap.put(CPermissions.name, new CPermissions());
        commandMap.put(CNotify.name, new CNotify());
        commandMap.put(CList.name, new CList());
        commandMap.put(CEnable.name, new CEnable());
        commandMap.put(CDisable.name, new CDisable());
        commandMap.put(CInvite.name, new CInvite());
        commandMap.put(CMove.name, new CMove());
        commandMap.put(CQueue.name, new CQueue());
        commandMap.put(CDonate.name, new CDonate());
        commandMap.put(CServers.name, new CServers());
        commandMap.put(CAnnounce.name, new CAnnounce());

        options.add("`game` : Game name based on Twitch's list (must be the exact name to work !)");
        options.add("`team` : Twitch team name (ID as mentioned in the team link)");
        options.add("`channel` : Twitch channel name");
        options.add("`tag` : Word or group of words that must be present in the stream's title");
        options.add("`manager` : Discord user (must use the @ alias when using this option !)");
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        if (e.getChannel().getId().equals("131483070464393216") && e.getMessage().getContent().startsWith("!invite")){
            inviteFromGuild(e);
        }
        if (e.getMessage().getContent().startsWith("!streambot")){
            try{
                commands(e, e.getMessage().getContent().substring(11));
            } catch(StringIndexOutOfBoundsException sioobe){
                DiscordInstance.getInstance().addToQueue(new MessageItem(e.getChannel().getId(), MessageItem.Type.GUILD, new MessageBuilder()
                        .appendString("You must put a command behind `!streambot` !")
                        .build()));
            }
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e){
        if (e.getMessage().getContent().startsWith("!invite")){
            inviteFromDM(e);
        }
        if (e.getMessage().getContent().equals("!streambot servers")){
            commandMap.get("servers").execute(new MessageReceivedEvent(api, e.getResponseNumber(), e.getMessage()), "");
        }
        if (e.getMessage().getContent().startsWith("!streambot announce")){
            commandMap.get("announce").execute(new MessageReceivedEvent(api, e.getResponseNumber(), e.getMessage()), e.getMessage().getContent().substring(20));
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent e){
        LocalServer ls = ServerList.getInstance().getServer(e.getGuild().getId());
        /*
        Condition to avoid the bot from re-posting this message when Discord servers are unavailable
        When the bot is invited to a server, the LocalServer is the following :
        - Inactive server
        - No Tag, Game, Channel or Team
        In this case, the message is shown because it's most likely that the server has just been created in the database. In case it's not, it'll remind people that the bot is there :)
         */
        if(!ls.isActive() && ls.getGameList().isEmpty() && ls.getTagList().isEmpty() && ls.getTeamList().isEmpty() && ls.getUserList().isEmpty()){
            DiscordInstance.getInstance().addToQueue(new MessageItem(ServerList.getInstance().getServer(e.getGuild().getId()).getId(), MessageItem.Type.GUILD, new MessageBuilder()
                    .appendString("Hello ! I'm StreamBot ! Type `!streambot commands` to see the available commands !")
                    .build()));
        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e){
        ServerList.getInstance().removeServer(e.getGuild().getId());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e){
        if(e.getGuild().getId().equals("131483070464393216")){
            MessageItem message = new MessageItem(e.getUser().getPrivateChannel().getId(), MessageItem.Type.PRIVATE);
            MessageBuilder builder = new MessageBuilder();
            builder.appendString("Hello " + e.getUser().getUsername() + "!\n");
            builder.appendString("If you wish to add me to your server, type `!invite <invite link>` on the #invite channel of my server.\n");
            builder.appendString("Then, you can follow the guidelines in #faq to set me up!\n");
            builder.appendString("If you forgot the commands, type `!streambot commands` or `!streambot help` on **your** server.\n");
            builder.appendString("Don't hesitate to ask questions to Gyoo, my creator!\n");
            builder.appendString("Hope you'll enjoy my work!");
            message.setMessage(builder.build());
            DiscordInstance.getInstance().addToQueue(message);
        }
    }

    private void inviteFromGuild(GuildMessageReceivedEvent e){
        String[] strings = e.getMessage().getContent().split(" ");
        InviteUtil.Invite i = InviteUtil.resolve(strings[1]);
        if(null == i){
            e.getChannel().sendMessage(new MessageBuilder()
                    .appendString("Error : Could not resolve invite link. Make sure it is an actual invite link, and try with a newly generated one.")
                    .build());
            return;
        }
        if(invite(new MessageReceivedEvent(api, e.getResponseNumber(), e.getMessage()), i)){
            DiscordInstance.getInstance().addToQueue(new MessageItem(e.getChannel().getId(), MessageItem.Type.GUILD, new MessageBuilder()
                    .appendString("Added Streambot to server " + i.getGuildName() + " in channel #" + i.getChannelName() + " !")
                    .build()));
            Role userRole = e.getGuild().getRoles().stream().filter(role -> role.getName().equals("User")).findFirst().get();
            e.getGuild().getManager().addRoleToUser(e.getAuthor(), userRole).update();
        }
        else e.getChannel().sendMessage(new MessageBuilder()
                .appendString("Error : Streambot already settled on channel #" + api.getTextChannelById(ServerList.getInstance().getServer(i.getGuildId()).getId()).getName() + " for this server")
                .build());
    }

    private void inviteFromDM(PrivateMessageReceivedEvent e){
        String[] strings = e.getMessage().getContent().split(" ");
        InviteUtil.Invite i = InviteUtil.resolve(strings[1]);
        if(null == i){
            e.getChannel().sendMessage(new MessageBuilder()
                    .appendString("Error : Could not resolve invite link. Make sure it is an actual invite link, and try with a newly generated one.")
                    .build());
            return;
        }
        if(invite(new MessageReceivedEvent(api, e.getResponseNumber(), e.getMessage()), i)){
            DiscordInstance.getInstance().addToQueue(new MessageItem(e.getAuthor().getPrivateChannel().getId(), MessageItem.Type.PRIVATE, new MessageBuilder()
                    .appendString("Added Streambot to server " + i.getGuildName() + " in channel #" + i.getChannelName() + " !\n")
                    .appendString("For help and feedback, make sure to join my server and ask Gyoo to give you the User role !\n")
                    .appendString(InviteUtil
                            .createInvite(DiscordInstance.getInstance().getDiscord().getTextChannelById("131483070464393216"))
                            .getUrl())
                    .build()));
        }
        else e.getChannel().sendMessage(new MessageBuilder()
                .appendString("Error : Streambot already settled on channel #" + api.getTextChannelById(ServerList.getInstance().getServer(i.getGuildId()).getId()).getName() + " for this server")
                .build());
    }

    private boolean invite(MessageReceivedEvent e, InviteUtil.Invite i){
        if(!ServerList.getInstance().getMap().containsKey(i.getGuildId())){
            InviteUtil.join(i, api, null);
            LocalServer ls = new LocalServer(i.getChannelId(), i.getGuildId());
            ls.addManager(e.getAuthor().getId());
            ServerList.getInstance().addServer(i.getGuildId(), ls);
            return true;
        }
        else return false;
    }

    private void commands(GuildMessageReceivedEvent e, String command){
        String[] split = command.split(" ");
        String content = command.substring(command.indexOf(" ") + 1);
        //TODO Make `help` its own command with a different content
        if(split[0].equals("commands") || split[0].equals("help")){
            MessageBuilder builder = new MessageBuilder();
            builder.appendString("`!streambot <command>`\n");
            builder.appendString("`commands` || `help` : List of available commands\n");
            commandMap.entrySet().stream().filter(c -> c.getValue().isAllowed(e.getGuild().getId(), e.getAuthor().getId())).forEach(c -> builder.appendString(c.getValue().getDescription() + "\n"));
            builder.appendString("\n*Options* :\n");
            for (String option : options){
                builder.appendString(option + "\n");
            }
            DiscordInstance.getInstance().addToQueue(new MessageItem(e.getChannel().getId(), MessageItem.Type.GUILD, builder.build()));
            return;
        }
        if(commandMap.containsKey(split[0]))
            commandMap.get(split[0]).execute(new MessageReceivedEvent(api, e.getResponseNumber(), e.getMessage()), content);
        else
            DiscordInstance.getInstance().addToQueue(new MessageItem(e.getChannel().getId(), MessageItem.Type.GUILD, new MessageBuilder()
                    .appendString("Unknown command")
                    .build()));
    }

}
