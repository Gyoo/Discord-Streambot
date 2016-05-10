package entity;

import javax.persistence.*;

@Entity
@Table(name = "manager", schema = "streambot", catalog = "")
public class ManagerEntity {

    @Id
    @Column(name="ID", nullable = false)
    private int ID;

    @ManyToOne
    @JoinColumn(name = "ServerID", nullable = false)
    private GuildEntity guild;

    @Basic
    @Column(name = "UserID", nullable = false)
    private long userId;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public GuildEntity getGuild() {
        return guild;
    }

    public void setGuild(GuildEntity guild) {
        this.guild = guild;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ManagerEntity that = (ManagerEntity) o;

        if (ID != that.ID) return false;
        if (userId != that.userId) return false;
        return guild != null ? guild.equals(that.guild) : that.guild == null;

    }

    @Override
    public int hashCode() {
        int result = ID;
        result = 31 * result + (guild != null ? guild.hashCode() : 0);
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        return result;
    }
}
