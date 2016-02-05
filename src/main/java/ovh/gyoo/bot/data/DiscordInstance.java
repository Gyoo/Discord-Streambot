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

public class DiscordInstance {
    private static DiscordInstance ourInstance = new DiscordInstance();
    private JDA discord;

    public static DiscordInstance getInstance() {
        return ourInstance;
    }
    public void resetInstance(){
        setInstance();
    }

    public JDA getDiscord(){return discord;}

    private DiscordInstance() {
        setInstance();
    }

    private void setInstance(){
        try {
            SAXBuilder sxb = new SAXBuilder();
            Document document = sxb.build(new File("src/main/resources/credentials.xml")); // Make sure you have the file ! Not commited !
            Element root = document.getRootElement();
            String mail = root.getChild("mail").getText();
            String pass = root.getChild("password").getText();
            discord = new JDABuilder(mail, pass).buildBlocking();
            discord.addEventListener(new DiscordListener(discord));
        } catch (LoginException | InterruptedException | JDOMException | IOException e) {
            Logger.writeToErr(e.getMessage());
        }
    }

}
