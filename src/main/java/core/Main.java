package core;

import common.Logger;
import common.PropertiesReader;
import common.util.HibernateUtil;
import core.platform.PTwitch;
import core.platform.Platform;
import dao.Dao;
import entity.GuildEntity;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.impl.JDAImpl;
import net.dv8tion.jda.utils.SimpleLog;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import ws.discord.DiscordController;
import ws.discord.messages.MessageConsumer;
import ws.discord.messages.MessageFinder;
import ws.discord.messages.MessageHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(final String[] args) throws Exception {
        try {
            SimpleLog.addFileLogs(null,new File("err" + System.currentTimeMillis() + ".txt"));
        } catch (IOException e) {
            Logger.writeToErr(e, "");
        }
        Dao dao = new Dao();
        List<Platform> platforms;
        String token = PropertiesReader.getInstance().getProp().getProperty("bot.token");
        DiscordController discordController = new DiscordController(dao);
        JDA jda = new JDABuilder()
                .setBotToken(token)
                .addListener(discordController)
                .buildBlocking();
        discordController.setJda(jda);

        Thread messageConsumer = new MessageConsumer(MessageHandler.getQueue(), jda, dao);
        messageConsumer.start();

        Thread messageFinder = new MessageFinder(MessageHandler.getDeleteQueue(), jda);
        messageFinder.start();

        platforms = setPlatforms(dao, jda);

        platforms.forEach(Platform::checkStillOnline);
        int ticks = 0;
        while(true){

            if(!((JDAImpl)jda).getClient().isConnected()){
                while (!((JDAImpl)jda).getClient().isConnected()){
                    Thread.sleep(1000);
                }
                discordController.setJda(jda);
                platforms = setPlatforms(dao, jda);
                continue;
            }

            if(!messageConsumer.isAlive()){
                messageConsumer = new MessageConsumer(MessageHandler.getQueue(), jda, dao);
                messageConsumer.start();
            }

            if(!messageFinder.isAlive()){
                messageFinder = new MessageFinder(MessageHandler.getDeleteQueue(), jda);
                messageFinder.start();
            }

            if(ticks < 30){
                Session session = HibernateUtil.getSession();
                List<Long> guildIDs = session.createCriteria(GuildEntity.class)
                        .setProjection(Projections.property("id")).list();
                List<Guild> connectedGuilds = jda.getGuilds();
                for(Guild connectedGuild : connectedGuilds){
                    Long connectedGuildID = Long.parseLong(connectedGuild.getId());
                    if(guildIDs.contains(connectedGuildID)){
                        for(Platform platform : platforms){
                            platform.checkStreams(connectedGuildID);
                        }
                    }
                }
                ticks++;
            }
            else{
                platforms.forEach(Platform::checkStillOnline);
                ticks = 0;
            }

            Thread.sleep(20000);


        }

    }

    public static List<Platform> setPlatforms(Dao dao, JDA jda){
        List<Platform> platforms = new ArrayList<>();
        platforms.add(new PTwitch(dao, jda));
        return platforms;
    }
}
