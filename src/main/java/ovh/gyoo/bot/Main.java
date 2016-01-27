package ovh.gyoo.bot;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.impl.JDAImpl;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.LocalServer;
import ovh.gyoo.bot.data.ServerList;
import ovh.gyoo.bot.handlers.TwitchChecker;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws LoginException, InterruptedException {
        // Twitch
        TwitchChecker.getInstance();
        //Discord
        DiscordInstance.getInstance();
        //Backup data after restart
        File f = new File("ServerList.ser");
        if(f.exists()){
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("ServerList.ser"));
                Map<String, LocalServer> tempInstance = (Map<String, LocalServer>) ois.readObject();
                ois.close();
                for(Map.Entry<String, LocalServer> entry : tempInstance.entrySet()){
                    LocalServer ls = entry.getValue();
                    TextChannel server = DiscordInstance.getInstance().getDiscord().getTextChannelById(ls.getId());
                    if(null != server) ls.setServerID(server.getGuild().getId());
                    else continue;
                    ServerList.getInstance().addServer(entry.getKey(), ls);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
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

            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ServerList.ser"));
                oos.writeObject(ServerList.getInstance().getMap());
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }


}
