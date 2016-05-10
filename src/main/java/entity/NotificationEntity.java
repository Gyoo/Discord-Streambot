package entity;

import javax.persistence.*;

@Entity
@Table(name = "notification", schema = "streambot", catalog = "")
public class NotificationEntity {

    @Id
    @Column(name="ID", nullable = false)
    private int ID;

    @ManyToOne
    @JoinColumn(name = "ServerID", nullable = false)
    private GuildEntity guild;

    @Basic
    @Column(name = "UserID", nullable = false)
    private Long userId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationEntity that = (NotificationEntity) o;

        if (ID != that.ID) return false;
        if (guild != null ? !guild.equals(that.guild) : that.guild != null) return false;
        return userId != null ? userId.equals(that.userId) : that.userId == null;

    }

    @Override
    public int hashCode() {
        int result = ID;
        result = 31 * result + (guild != null ? guild.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
