package dao;

import common.util.HibernateUtil;
import entity.*;
import org.hibernate.*;
import org.hibernate.criterion.Projections;

import java.util.ArrayList;
import java.util.List;

public class Dao {

    protected SessionFactory sessionFactory;

    public Dao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public <T> T save(final T o) {
        Transaction trans=sessionFactory.getCurrentSession().beginTransaction();
        T t = (T) sessionFactory.getCurrentSession().save(o);
        trans.commit();
        return t;
    }

    public <T> void deleteLongId(final Class<T> type, final Long id) {
        Transaction trans=sessionFactory.getCurrentSession().beginTransaction();
        T t = (T) sessionFactory.getCurrentSession().get(type, id);
        sessionFactory.getCurrentSession().delete(t);
        trans.commit();
    }

    public <T> void deleteIntId(final Class<T> type, final int id) {
        Transaction trans=sessionFactory.getCurrentSession().beginTransaction();
        T t = (T) sessionFactory.getCurrentSession().get(type, id);
        sessionFactory.getCurrentSession().delete(t);
        trans.commit();
    }

    /***/
    public <T> T getLongId(final Class<T> type, final Long id) {
        Transaction trans=sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().setCacheMode(CacheMode.IGNORE);
        T t = (T) sessionFactory.getCurrentSession().get(type, id);
        sessionFactory.getCurrentSession().setCacheMode(CacheMode.IGNORE);
        trans.commit();
        return t;
    }

    /***/
    public <T> T getLongId(final Class<T> type, final String id) {
        return getLongId(type, Long.parseLong(id));
    }

    /***/
    public <T> T getIntId(final Class<T> type, final Integer id) {
        Transaction trans=sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().setCacheMode(CacheMode.IGNORE);
        T t = (T) sessionFactory.getCurrentSession().get(type, id);
        sessionFactory.getCurrentSession().setCacheMode(CacheMode.IGNORE);
        trans.commit();
        return t;
    }

    /***/
    public <T> T getIntId(final Class<T> type, final String id) {
        return getIntId(type, Integer.parseInt(id));
    }

    /***/
    public <T> T merge(final T o) {
        Transaction trans=sessionFactory.getCurrentSession().beginTransaction();
        T t = (T) sessionFactory.getCurrentSession().merge(o);
        trans.commit();
        return t;
    }

    /***/
    public <T> void saveOrUpdate(final T o) {
        Transaction trans=sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(o);
        trans.commit();
    }

    public <T> List<T> getAll(final Class<T> type) {
        Transaction trans=sessionFactory.getCurrentSession().beginTransaction();
        try{
            final Session session = sessionFactory.getCurrentSession();
            final Query query = session.createQuery("from " + type.getName());
            List<T> response = query.list();
            trans.commit();
            return response;
        }catch(ObjectNotFoundException e){
            trans.commit();
            this.deleteAllTraceOfId(e.getIdentifier().toString());
            return this.getAll(type);
        }
    }

    private void deleteAllTraceOfId(final String ServerId){
        List<String> classNames = new ArrayList<>();
        classNames.add(ChannelEntity.class.getName());
        classNames.add(GameEntity.class.getName());
        classNames.add(ManagerEntity.class.getName());
        classNames.add(NotificationEntity.class.getName());
        classNames.add(PermissionEntity.class.getName());
        classNames.add(QueueitemEntity.class.getName());
        classNames.add(StreamEntity.class.getName());
        classNames.add(TagEntity.class.getName());
        classNames.add(TeamEntity.class.getName());
        for(String className : classNames){
            Transaction trans=sessionFactory.getCurrentSession().beginTransaction();
            final Session session = sessionFactory.getCurrentSession();
            session.createQuery("DELETE " + className + " WHERE guild = :serverid").setString("serverid", ServerId).executeUpdate();
            trans.commit();
        }

    }

    public <T> Long count(final Class<T> type){
        Transaction trans=sessionFactory.getCurrentSession().beginTransaction();
        try {
            final Session session = sessionFactory.getCurrentSession();
            return (Long) session.createCriteria(type).setProjection(Projections.rowCount()).uniqueResult();
        }finally {
            trans.commit();
        }
    }

    public <T> List<T> getForGuild(final Class<T> type, final String serverId){
        Transaction trans=sessionFactory.getCurrentSession().beginTransaction();
        try{
            final Session session = sessionFactory.getCurrentSession();
            final Query query = session.createQuery("from " + type.getName() + " where guild = :serverid").setString("serverid", serverId);
            List<T> response = query.list();
            trans.commit();
            return response;
        }catch(ObjectNotFoundException e){
            trans.commit();
            this.deleteAllTraceOfId(e.getIdentifier().toString());
            return this.getAll(type);
        }
    }
}
