package entity;

import javax.persistence.*;

@Entity
@Table(name = "stream", schema = "streambot", catalog = "")
public class StreamEntity{

    @Id
    @Column(name = "ID", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "ServerID", nullable = false)
    private GuildEntity guild;

    @OneToOne
    @JoinColumn(name = "PlatformID", nullable = false)
    private PlatformEntity platform;

    @Basic
    @Column(name = "Channel_name", nullable = false, length = -1)
    private String channelName;

    @Basic
    @Column(name = "Stream_title", nullable = true, length = -1)
    private String streamTitle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GuildEntity getGuild() {
        return guild;
    }

    public void setGuild(GuildEntity guild) {
        this.guild = guild;
    }

    public PlatformEntity getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformEntity platform) {
        this.platform = platform;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getStreamTitle() {
        return streamTitle;
    }

    public void setStreamTitle(String streamTitle) {
        this.streamTitle = streamTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StreamEntity that = (StreamEntity) o;

        if (id != that.id) return false;
        if (platform != that.platform) return false;
        if (guild != null ? !guild.equals(that.guild) : that.guild != null) return false;
        return channelName != null ? !channelName.equals(that.channelName) : that.channelName != null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (guild != null ? guild.hashCode() : 0);
        result = 31 * result + platform.hashCode();
        result = 31 * result + (channelName != null ? channelName.hashCode() : 0);
        result = 31 * result + (streamTitle != null ? streamTitle.hashCode() : 0);
        return result;
    }
}
