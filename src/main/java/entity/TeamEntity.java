package entity;

import javax.persistence.*;

@Entity
@Table(name = "team", schema = "streambot", catalog = "")
public class TeamEntity {

    @Id
    @Column(name = "ID", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "ServerID", nullable = false)
    private GuildEntity guild;

    @Basic
    @Column(name = "Name", nullable = true, length = -1)
    private String name;

    @OneToOne
    @JoinColumn(name = "PlatformID", nullable = false)
    private PlatformEntity platform;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GuildEntity getGuild() {
        return guild;
    }

    public void setGuild(GuildEntity guild) {
        this.guild = guild;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlatformEntity getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformEntity platform) {
        this.platform = platform;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamEntity that = (TeamEntity) o;

        if (id != that.id) return false;
        if (guild != null ? !guild.equals(that.guild) : that.guild != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return platform != null ? platform.equals(that.platform) : that.platform == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (guild != null ? guild.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (platform != null ? platform.hashCode() : 0);
        return result;
    }
}
