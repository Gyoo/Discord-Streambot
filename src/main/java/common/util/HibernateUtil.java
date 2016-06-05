package common.util;

import common.PropertiesReader;
import entity.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private static final SessionFactory ourSessionFactory;
    private static final ServiceRegistry serviceRegistry;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.mergeProperties(PropertiesReader.getInstance().getProp());
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(ChannelEntity.class);
            configuration.addAnnotatedClass(GameEntity.class);
            configuration.addAnnotatedClass(GuildEntity.class);
            configuration.addAnnotatedClass(ManagerEntity.class);
            configuration.addAnnotatedClass(NotificationEntity.class);
            configuration.addAnnotatedClass(PlatformEntity.class);
            configuration.addAnnotatedClass(QueueitemEntity.class);
            configuration.addAnnotatedClass(StreamEntity.class);
            configuration.addAnnotatedClass(TagEntity.class);
            configuration.addAnnotatedClass(TeamEntity.class);
            configuration.addAnnotatedClass(PermissionEntity.class);
            configuration.addAnnotatedClass(CommandEntity.class);

            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static final ThreadLocal session = new ThreadLocal();

    public static SessionFactory getSessionFactory() {
        return ourSessionFactory;
    }
}