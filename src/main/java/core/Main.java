package core;

import common.util.HibernateUtil;
import core.platform.PTwitch;
import core.platform.Platform;
import dao.Dao;
import entity.*;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Guild;
import old.LocalServer;
import old.Logger;
import old.Permissions;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import ws.discord.messages.MessageConsumer;
import ws.discord.messages.MessageHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(final String[] args) throws Exception {
        Dao dao = new Dao();
        List<Platform> platforms = new ArrayList<>();
        JDA jda = new JDABuilder().setBotToken("OTk4NTI5MDQ1MDQwMDQ2MDg.CfObZw.km7UY01em2JAo8ZqCMPV8HfAwqo").buildBlocking();

        Thread messageConsumer = new MessageConsumer(MessageHandler.getQueue(), jda);
        messageConsumer.start();

        platforms.add(new PTwitch(dao, jda));

        File f = new File("ServerList.xml");
        if(f.exists()){
            List<LocalServer> servers = Logger.loadData("ServerList.xml");
            for(LocalServer server : servers){
                GuildEntity entity = dao.getLongId(GuildEntity.class, Long.parseLong(server.getServerID()));
                if(entity != null){
                    for(Map.Entry<String, Permissions> entry : server.getPermissionsMap().entrySet()){
                        for(Map.Entry<String, Integer> permEntry : entry.getValue().getPerms().entrySet()){
                            PermissionEntity permissionEntity = new PermissionEntity();
                            permissionEntity.setGuild(entity);
                            permissionEntity.setLevel(permEntry.getValue());
                            if(permEntry.getKey().equals("everyone")) permissionEntity.setRoleId(0L);
                            else permissionEntity.setRoleId(Long.parseLong(permEntry.getKey()));
                            CommandEntity commandEntity = null;
                            switch(entry.getKey()) {
                                case "add":
                                    commandEntity = dao.getLongId(CommandEntity.class, 1L);
                                    break;
                                case "remove":
                                    commandEntity = dao.getLongId(CommandEntity.class, 2L);
                                    break;
                                default:
                                    break;
                            }
                            permissionEntity.setCommand(commandEntity);
                            dao.saveOrUpdate(permissionEntity);
                        }
                    }
                }
            }
        }


        Session session = HibernateUtil.getSession();
        List<Long> guildIDs = session.createCriteria(GuildEntity.class)
                .setProjection(Projections.property("id")).list();
        while(true){
            List<Guild> connectedGuilds = jda.getGuilds();
            for(Guild connectedGuild : connectedGuilds){
                Long connectedGuildID = Long.parseLong(connectedGuild.getId());
                if(guildIDs.contains(connectedGuildID)){
                    for(Platform platform : platforms){
                        platform.checkStreams(connectedGuildID);
                    }
                }
            }

            for(Platform platform : platforms){
                platform.checkStillOnline();
            }

            Thread.sleep(20000);
        }

    }
}
