package ovh.gyoo.bot.data;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import ovh.gyoo.bot.listeners.DiscordListener;
import ovh.gyoo.bot.writer.Logger;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class DiscordInstance {
    private static DiscordInstance ourInstance = new DiscordInstance();
    private static final LinkedBlockingQueue<MessageItem> queue = new LinkedBlockingQueue<>();
    private JDA discord;

    public static DiscordInstance getInstance() {
        return ourInstance;
    }
    public void resetInstance(){
        setInstance();
    }

    public JDA getDiscord(){return discord;}

    public void addToQueue(MessageItem m){
        synchronized (queue){
            queue.add(m);
            queue.notify();
        }
    }

    public LinkedBlockingQueue<MessageItem> getQueue(){return queue;}

    private DiscordInstance() {
        setInstance();
    }

    private void setInstance(){
        try {
            discord = new JDABuilder().setBotToken("OTk4NTI5MDQ1MDQwMDQ2MDg.CfObZw.km7UY01em2JAo8ZqCMPV8HfAwqo").buildBlocking();
            discord.addEventListener(new DiscordListener(discord));
        } catch (LoginException | InterruptedException e) {
            Logger.writeToErr(e, "");
        }
    }

}
