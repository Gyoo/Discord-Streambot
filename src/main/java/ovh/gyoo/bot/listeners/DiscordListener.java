package ovh.gyoo.bot.listeners;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.*;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.InviteUtil;
import ovh.gyoo.bot.handlers.TwitchChecker;
import ovh.gyoo.bot.data.LocalServer;
import ovh.gyoo.bot.data.ServerList;

import java.util.ArrayList;
import java.util.List;

public class DiscordListener extends ListenerAdapter {
    JDA api;
    List<String> commands = new ArrayList<>();
    List<String> options = new ArrayList<>();

    public DiscordListener(JDA api){
        this.api = api;

        commands.add("`commands` : List of available commands");
        commands.add("`streams` : List of online streams");
        commands.add("`add <option> <content>` : Add data to the bot (see options below)");
        commands.add("`remove <option> <content>` : Remove data from the bot");
        commands.add("`list <option>` : List data from the bot");
        commands.add("`enable` : Activates bot after configuration");
        commands.add("`disable` : Deactivates bot");
        commands.add("`invite` : Gives an invite link so people can get the bot on their own server !");
        commands.add("`move <channel>` : Moves the bot announces to another channel (Must use the #channel identifier !)");

        options.add("`game` : Game name based on Twitch's list (must be the exact name to work !)");
        options.add("`channel` : Twitch channel name");
        options.add("`tag` : Word or group of words that must be present in the stream's title");
        options.add("`manager` : Discord user (must use the @ alias when using this option !)");
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        if (e.getChannel().getId().equals("131483070464393216") && e.getMessage().getContent().startsWith("!invite")){
            invite(e);
        }
        if (e.getMessage().getContent().startsWith("!streambot")){
            commands(e, e.getMessage().getContent().substring(11));
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e){
        if (e.getMessage().getContent().equals("!streambot servers")){
            e.getChannel().sendMessage(commandServer());
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent e){
        api.getTextChannelById(ServerList.getInstance().getServer(e.getGuild().getId()).getId())
                .sendMessage(new MessageBuilder()
                    .appendString("Hello ! I'm StreamBot ! Type `!streambot commands` to see the available commands !")
                    .build());
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e){
        ServerList.getInstance().removeServer(e.getGuild().getId());
    }

    private void invite(GuildMessageReceivedEvent e){
        String[] strings = e.getMessage().getContent().split(" ");
        InviteUtil.Invite i = InviteUtil.resolve(strings[1]);
        InviteUtil.join(i, api);
        if(null == ServerList.getInstance().getServer(i.getGuildId())){
            LocalServer ls = new LocalServer(i.getChannelId(), i.getGuildId());
            ls.addManager(e.getAuthor().getId());
            ServerList.getInstance().addServer(i.getGuildId(), ls);
            e.getChannel().sendMessage(new MessageBuilder()
                    .appendString("Added Streambot to server " + i.getGuildName() + " in channel #" + i.getChannelName() + " !")
                    .build());
            Role userRole = e.getGuild().getRoles().stream().filter(role -> role.getName().equals("User")).findFirst().get();
            e.getGuild().getManager().addRoleToUser(e.getAuthor(), userRole);
        }
        else e.getChannel().sendMessage(new MessageBuilder()
                .appendString("Error : Streambot already settled on channel #" + api.getTextChannelById(ServerList.getInstance().getServer(i.getGuildId()).getId()).getName() + " for this server")
                .build());
    }

    private void commands(GuildMessageReceivedEvent e, String command){
        String[] split = command.split(" ");
        String content;
        String[] contents;
        switch(split[0]){
            case "commands":
                e.getChannel().sendMessage(commandCommands());
                break;
            case "streams":
                e.getAuthor().getPrivateChannel().sendMessage(commandStreams(e.getGuild().getId()));
                break;
            case "enable":
                e.getChannel().sendMessage(commandActivate(e.getGuild().getId()));
                break;
            case "disable":
                e.getChannel().sendMessage(commandDisactivate(e.getGuild().getId()));
                break;
            case "invite":
                e.getChannel().sendMessage(commandInvite());
                break;
            case "move":
                TextChannel tc = api.getTextChannelById(split[1].substring(2, split[1].length()-1));
                e.getChannel().sendMessage(commandMove(e.getGuild().getId(), tc));
                break;
            case "add":
                if(ServerList.getInstance().getServer(e.getGuild().getId()).getManagers().contains(e.getAuthor().getId())){
                    content = command.substring(command.indexOf(" ", command.indexOf(" ")+1) + 1);
                    contents = content.split("\\|");
                    switch (split[1]){
                        case "game":
                            e.getChannel().sendMessage(commandAddGame(e.getGuild().getId(), contents));
                            break;
                        case "channel":
                            e.getChannel().sendMessage(commandAddChannel(e.getGuild().getId(), contents));
                            break;
                        case "tag":
                            e.getChannel().sendMessage(commandAddTag(e.getGuild().getId(), contents));
                            break;
                        case "manager":
                            e.getChannel().sendMessage(commandAddManagers(e.getGuild().getId(), e.getMessage().getMentionedUsers()));
                            break;
                        default:
                            e.getChannel().sendMessage(new MessageBuilder()
                                    .appendString("Unknown command")
                                    .build());
                            break;
                    }
                }
                else e.getChannel().sendMessage(new MessageBuilder().appendString("You are not allowed to use this command").build());
                break;
            case "remove":
                if(ServerList.getInstance().getServer(e.getGuild().getId()).getManagers().contains(e.getAuthor().getId())){
                    content = command.substring(command.indexOf(" ", command.indexOf(" ")+1) + 1);
                    contents = content.split("\\|");
                    switch (split[1]){
                        case "game":
                            e.getChannel().sendMessage(commandRemoveGame(e.getGuild().getId(), contents));
                            break;
                        case "channel":
                            e.getChannel().sendMessage(commandRemoveChannel(e.getGuild().getId(), contents));
                            break;
                        case "tag":
                            e.getChannel().sendMessage(commandRemoveTag(e.getGuild().getId(), contents));
                            break;
                        case "manager":
                            e.getChannel().sendMessage(commandRemoveManagers(e.getGuild().getId(), e.getAuthor().getId(), e.getMessage().getMentionedUsers()));
                            break;
                        default:
                            e.getChannel().sendMessage(new MessageBuilder()
                                    .appendString("Unknown command")
                                    .build());
                            break;
                    }
                }
                else e.getChannel().sendMessage(new MessageBuilder().appendString("You are not allowed to use this command").build());
                break;
            case "list":
                switch (split[1]){
                    case "game":
                        e.getChannel().sendMessage(commandListGame(e.getGuild().getId()));
                        break;
                    case "channel":
                        e.getChannel().sendMessage(commandListChannel(e.getGuild().getId()));
                        break;
                    case "tag":
                        e.getChannel().sendMessage(commandListTag(e.getGuild().getId()));
                        break;
                    case "manager":
                        e.getChannel().sendMessage(commandListManagers(e.getGuild().getId()));
                        break;
                    default:
                        e.getChannel().sendMessage(new MessageBuilder()
                                .appendString("Unknown command")
                                .build());
                        break;
                }
                break;
            default:
                e.getChannel().sendMessage(new MessageBuilder()
                    .appendString("Unknown command")
                    .build());
        }
    }

    private Message commandServer(){
        MessageBuilder builder = new MessageBuilder();
        for(LocalServer ls : ServerList.getInstance().getServerList()){
            if(ls.isActive()) builder.appendString("**" + api.getGuildById(ls.getServerID()).getName() + "** | ");
            else builder.appendString(api.getGuildById(ls.getServerID()).getName() + " | ");
        }
        return builder.build();
    }

    private Message commandCommands(){
        MessageBuilder builder = new MessageBuilder();
        builder.appendString("`!streambot <command>`\n");
        for (String command : commands){
            builder.appendString(command + "\n");
        }
        builder.appendString("\n*Options* :\n");
        for (String option : options){
            builder.appendString(option + "\n");
        }
        return builder.build();
    }

    private Message commandActivate(String serverId){
        ServerList.getInstance().getServer(serverId).activate();
        return new MessageBuilder().appendString("Bot activated !").build();
    }

    private Message commandDisactivate(String serverId){
        ServerList.getInstance().getServer(serverId).disactivate();
        return new MessageBuilder().appendString("Bot disactivated !").build();
    }

    private Message commandInvite(){
        return new MessageBuilder()
                .appendString(InviteUtil
                        .createInvite(api.getTextChannelById("131483070464393216"),api)
                        .getUrl())
                .build();
    }

    private Message commandMove(String serverId, Channel channel){
        ServerList.getInstance().getServer(serverId).setId(channel.getId());
        return new MessageBuilder()
                .appendString("Announces will now be done in #" + channel.getName() + " !")
                .build();
    }

    private Message commandStreams(String serverId) {
        return TwitchChecker.getInstance().checkStreamsPM(serverId);
    }

    private Message commandAddGame(String serverId, String[] game){
        MessageBuilder mb = new MessageBuilder();
        for(String s : game){
            s = s.trim();
            boolean res = ServerList.getInstance().getServer(serverId).addGame(s);
            if(res)
                mb.appendString("Game " + s + " added to the game list\n");
            else
                mb.appendString("Game " + s + " is already in the game list\n");
        }
        return mb.build();
    }

    private Message commandRemoveGame(String serverId, String[] game){
        MessageBuilder mb = new MessageBuilder();
        for(String s : game){
            s = s.trim();
            boolean res = ServerList.getInstance().getServer(serverId).removeGame(s);
            if(res)
                mb.appendString("Game " + s + " removed from the game list\n");
            else
                mb.appendString("Game " + s + " is not in the game list\n");
        }
        return mb.build();
    }

    private Message commandListGame(String serverId){
        List<String> list = ServerList.getInstance().getServer(serverId).getGameList();
        MessageBuilder builder = new MessageBuilder();
        for(String s : list){
            builder.appendString(s + " | ");
        }
        return builder.build();
    }

    private Message commandAddChannel(String serverId, String[] channels){
        MessageBuilder mb = new MessageBuilder();
        for(String s : channels){
            s = s.trim();
            boolean res = ServerList.getInstance().getServer(serverId).addUser(s.replaceAll("`", "").toLowerCase());
            if(res)
                mb.appendString("Channel " + s + " added to the channels list\n");
            else
                mb.appendString("Channel " + s + " is already in the channels list\n");
        }
        return mb.build();
    }

    private Message commandRemoveChannel(String serverId, String[] channels){
        MessageBuilder mb = new MessageBuilder();
        for(String s : channels){
            s = s.trim();
            boolean res = ServerList.getInstance().getServer(serverId).removeUser(s.replaceAll("`", "").toLowerCase());
            if(res)
                mb.appendString("Channel " + s + " removed from the channels list\n");
            else
                mb.appendString("Channel " + s + " is not in the channels list\n");
        }
        return mb.build();
    }

    private Message commandListChannel(String serverId){
        List<String> list = ServerList.getInstance().getServer(serverId).getUserList();
        MessageBuilder builder = new MessageBuilder();
        for(String s : list){
            builder.appendString(s + " | ");
        }
        return builder.build();
    }

    private Message commandAddTag(String serverId, String[] tags){
        MessageBuilder mb = new MessageBuilder();
        for(String s : tags){
            s = s.trim();
            boolean res = ServerList.getInstance().getServer(serverId).addTag(s);
            if(res)
                mb.appendString("Tag " + s + " added to the tags list\n");
            else
                mb.appendString("Tag " + s + " is already in the tags list\n");
        }
        return mb.build();
    }

    private Message commandRemoveTag(String serverId, String[] tags){
        MessageBuilder mb = new MessageBuilder();
        for(String s : tags){
            s = s.trim();
            boolean res = ServerList.getInstance().getServer(serverId).removeTag(s);
            if(res)
                mb.appendString("Tag " + s + " removed from the tags list\n");
            else
                mb.appendString("Tag " + s + " is not in the tags list\n");
        }
        return mb.build();
    }

    private Message commandListTag(String serverId){
        List<String> list = ServerList.getInstance().getServer(serverId).getTagList();
        MessageBuilder builder = new MessageBuilder();
        for(String s : list){
            builder.appendString(s + " | ");
        }
        return builder.build();
    }

    private Message commandAddManagers(String serverId, List<User> users){
        MessageBuilder builder = new MessageBuilder();
        for(User u : users){
            boolean res = ServerList.getInstance().getServer(serverId).addManager(u.getId());
            if(res)
                builder.appendString("User " + u.getUsername() + " added to the managers list\n");
            else
                builder.appendString("User " + u.getUsername() + " is already in the managers list\n");
        }
        return builder.build();
    }

    private Message commandRemoveManagers(String serverId, String userId, List<User> users){
        MessageBuilder builder = new MessageBuilder();
        for(User u : users){
            if(ServerList.getInstance().getServer(serverId).getManagers().size() == 1){
                builder.appendString("Cannot remove managers : There must be at least one manager per server\n");
                break;
            }
            if(userId.equals(u.getId())){
                builder.appendString("You cannot remove yourself !\n");
                continue;
            }
            boolean res = ServerList.getInstance().getServer(serverId).removeManager(u.getId());
            if(res)
                builder.appendString("User " + u.getUsername() + " removed from the managers list\n");
            else
                builder.appendString("User " + u.getUsername() + " is not in the managers list\n");
        }
        return builder.build();
    }

    private Message commandListManagers(String serverId){
        List<String> list = ServerList.getInstance().getServer(serverId).getManagers();
        MessageBuilder builder = new MessageBuilder();
        for(String u : list){
            builder.appendString(api.getUserById(u).getUsername() + " | ");
        }
        return builder.build();
    }
}
