package dao;

import common.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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

    public void delete(final Object object) {
        Transaction trans=sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(object);
        trans.commit();
    }

    /***/
    public <T> T getLongId(final Class<T> type, final Long id) {
        sessionFactory.getCurrentSession().beginTransaction();
        T t = (T) sessionFactory.getCurrentSession().get(type, id);
        return t;
    }

    /***/
    public <T> T getLongId(final Class<T> type, final String id) {
        return getLongId(type, Long.parseLong(id));
    }

    /***/
    public <T> T getIntId(final Class<T> type, final Integer id) {
        sessionFactory.getCurrentSession().beginTransaction();
        T t = (T) sessionFactory.getCurrentSession().get(type, id);
        return t;
    }

    /***/
    public <T> T getIntId(final Class<T> type, final String id) {
        return getIntId(type, Integer.parseInt(id));
    }

    /***/
    public <T> T merge(final T o) {
        sessionFactory.getCurrentSession().beginTransaction();
        return (T) sessionFactory.getCurrentSession().merge(o);
    }

    /***/
    public <T> void saveOrUpdate(final T o) {
        Transaction trans=sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(o);
        trans.commit();
    }

    public <T> List<T> getAll(final Class<T> type) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(type);
        return crit.list();
    }
}
