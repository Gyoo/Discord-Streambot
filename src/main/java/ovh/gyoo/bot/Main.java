package ovh.gyoo.bot;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.impl.JDAImpl;
import net.dv8tion.jda.utils.InviteUtil;
import net.dv8tion.jda.utils.SimpleLog;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.LocalServer;
import ovh.gyoo.bot.data.MessageItem;
import ovh.gyoo.bot.data.ServerList;
import ovh.gyoo.bot.handlers.TwitchChecker;
import ovh.gyoo.bot.listeners.MessageConsumer;
import ovh.gyoo.bot.writer.Logger;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class Main {

    public static void main(String[] args) throws LoginException, InterruptedException {
        boolean startup = true;
        try {
            SimpleLog.addFileLogs(null,new File("err" + System.currentTimeMillis() + ".txt"));
        } catch (IOException e) {
            Logger.writeToErr(e, "");
        }
        // Twitch
        TwitchChecker.getInstance();
        // Backup data after restart
        File f = new File("ServerList.xml");
        if(f.exists()){
            List<LocalServer> servers = Logger.loadData("ServerList.xml");
            for(LocalServer server : servers){
                ServerList.getInstance().addServer(server.getServerID(), server);
            }
        }
        // Discord
        DiscordInstance.getInstance();
        // Message Consumer (anti rate limiter)
        Thread messageConsumer = new MessageConsumer(DiscordInstance.getInstance().getQueue());
        messageConsumer.start();

        // Look for deltas between Guilds list and data list. Kinda hacky to do it here but I can't place it elsewhere...
        List<Guild> guilds = DiscordInstance.getInstance().getDiscord().getGuilds();
        Map<String, String> inactiveManagers = new HashMap<>();
        for(Guild guild : guilds){
            if(guild.getName().equals("Discord Bots"))
                continue;
            LocalServer ls = ServerList.getInstance().getServer(guild.getId());
            if(ls == null){
                ls = new LocalServer(guild.getPublicChannel().getId(), guild.getId());
                ls.addManager(guild.getOwnerId());
                ServerList.getInstance().addServer(guild.getId(), ls);
                DiscordInstance.getInstance().addToQueue(new MessageItem(guild.getOwner().getPrivateChannel().getId(), MessageItem.Type.PRIVATE, new MessageBuilder()
                        .appendString("Thanks for inviting me ! By joining the following Guild, you can have access to guidelines to configure me properly, in the #faq channel : " +
                                InviteUtil.createInvite(DiscordInstance.getInstance().getDiscord().getTextChannelById("131483070464393216")).getUrl() + "\n" +
                                "You can also get news about the updates, alert about bugs or just ask questions !")
                        .build()));
            }
            //Reminder for inactive servers that the bot exists
//            else if(!ls.isActive()){
//                for(String manager : ls.getManagers()) {
//                    String servers = DiscordInstance.getInstance().getDiscord().getGuildById(ls.getServerID()).getName() + ((inactiveManagers.get(manager) != null) ? " | " + inactiveManagers.get(manager) : "");
//                    inactiveManagers.put(manager, servers);
//                }
//            }
        }
        for (Map.Entry<String, String> managerEntry : inactiveManagers.entrySet()){
            DiscordInstance.getInstance().addToQueue(new MessageItem(DiscordInstance.getInstance().getDiscord().getUserById(managerEntry.getKey()).getPrivateChannel().getId(), MessageItem.Type.PRIVATE, new MessageBuilder()
                    .appendString("Hey ! Don't forget you're a Streambot manager on : \n" + managerEntry.getValue() + "\n" +
                        "Use `!streambot commands` to see what configuration you can do, and don't forget `!streambot enable` to activate the announces !")
                    .build()));
        }

        int ticks = 0;
        while(true){

            // Waiting for reconnection
            if (!((JDAImpl) DiscordInstance.getInstance().getDiscord()).getClient().isConnected())
            {
                Thread.sleep(1000);
                continue;
            }

            // Restarting message consumer if it dies
            if(!messageConsumer.isAlive()) {
                messageConsumer = new MessageConsumer(DiscordInstance.getInstance().getQueue());
                messageConsumer.start();
            }

            // Checking the streams, and every half hour, checks if the streams are still online instead
            if(ticks < 180) {
                TwitchChecker.getInstance().checkStreams(startup);
                ticks++;
            }
            else {
                TwitchChecker.getInstance().checkStillOnline();
                ticks = 0;
            }

            startup = false;

            // Backing up data
            Logger.saveData(ServerList.getInstance().getMap(), "ServerList.xml");

            //Wait 10 seconds and restart the loop
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Logger.writeToErr(e,"");
                break;
            }
        }
    }


}
