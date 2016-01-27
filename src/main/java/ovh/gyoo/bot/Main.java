package ovh.gyoo.bot;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.impl.JDAImpl;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.LocalServer;
import ovh.gyoo.bot.data.ServerList;
import ovh.gyoo.bot.handlers.TwitchChecker;
import ovh.gyoo.bot.writer.Logger;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws LoginException, InterruptedException {
        // Twitch
        TwitchChecker.getInstance();
        //Discord
        DiscordInstance.getInstance();
        //Backup data after restart
        File f = new File("ServerList.xml");
        if(f.exists()){
            List<LocalServer> servers = Logger.loadData("ServerList.xml");
            for(LocalServer server : servers){
                ServerList.getInstance().addServer(server.getServerID(), server);
            }
        }

        int ticks = 0;
        while(true){

            if (!((JDAImpl) DiscordInstance.getInstance().getDiscord()).getClient().isConnected())
            {
                Thread.sleep(1000);
                DiscordInstance.getInstance().resetInstance();
                Thread.sleep(5000);
                if (!((JDAImpl) DiscordInstance.getInstance().getDiscord()).getClient().isConnected())
                {
                    //Uh, we didn't connect again. No internet? Bad password?   You should stop completely here.
                    //If you want, you could wait for like 5 minutes (Thread.sleep(300000)) and try the login again
                    break;
                }
            }

            if(ticks < 180) {
                TwitchChecker.getInstance().checkStreams();
                ticks++;
            }
            else {
                TwitchChecker.getInstance().checkStillOnline();
                ticks = 0;
            }

            Logger.saveData(ServerList.getInstance().getMap(), "ServerList.xml");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }


}
