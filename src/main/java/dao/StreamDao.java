package dao;

import entity.GuildEntity;
import entity.StreamEntity;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class StreamDao extends Dao {

    public StreamDao(){
        super();
    }

    public StreamEntity getByIdAndName(GuildEntity guild, String name){
        sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().setCacheMode(CacheMode.IGNORE);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(StreamEntity.class);
        criteria.add(Restrictions.eq("guild", guild))
                .add(Restrictions.eq("channelName", name));
        sessionFactory.getCurrentSession().setCacheMode(CacheMode.IGNORE);
        return (StreamEntity) criteria.uniqueResult();
    }

}
