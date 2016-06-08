package entity;

import javax.persistence.*;


@Entity
@Table(name = "queueitem", schema = "streambot", catalog = "")
public class QueueitemEntity {

    @Id
    @Column(name="ID", nullable = false)
    private int ID;

    @ManyToOne
    @JoinColumn(name = "ServerID", nullable = false)
    private GuildEntity guild;

    @Basic
    @Column(name = "UserID", nullable = false)
    private long userId;

    @Basic
    @Column(name = "Command", nullable = true, length = -1)
    private String command;

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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueueitemEntity that = (QueueitemEntity) o;

        if (ID != that.ID) return false;
        if (userId != that.userId) return false;
        if (guild != null ? !guild.equals(that.guild) : that.guild != null) return false;
        return command != null ? command.equals(that.command) : that.command == null;

    }

    @Override
    public int hashCode() {
        int result = ID;
        result = 31 * result + (guild != null ? guild.hashCode() : 0);
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (command != null ? command.hashCode() : 0);
        return result;
    }
}
