package dao;

import entity.GuildEntity;
import entity.StreamEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class StreamDao extends Dao {

    public StreamDao(){
        super();
    }

    public StreamEntity getByIdAndName(GuildEntity guild, String name){
        sessionFactory.getCurrentSession().beginTransaction();
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(StreamEntity.class);
        criteria.add(Restrictions.eq("guild", guild))
                .add(Restrictions.eq("channelName", name));
        return (StreamEntity) criteria.uniqueResult();
    }

}
