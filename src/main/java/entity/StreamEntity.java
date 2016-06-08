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

    @Basic
    @Column(name = "Game_name", nullable = true, length = -1)
    private String gameName;

    @Basic
    @Column(name = "MessageId", nullable = true)
    private Long messageId;

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

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StreamEntity that = (StreamEntity) o;

        if (id != that.id) return false;
        if (guild != null ? !guild.equals(that.guild) : that.guild != null) return false;
        if (platform != null ? !platform.equals(that.platform) : that.platform != null) return false;
        if (channelName != null ? !channelName.equals(that.channelName) : that.channelName != null) return false;
        if (streamTitle != null ? !streamTitle.equals(that.streamTitle) : that.streamTitle != null) return false;
        return gameName != null ? gameName.equals(that.gameName) : that.gameName == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (guild != null ? guild.hashCode() : 0);
        result = 31 * result + (platform != null ? platform.hashCode() : 0);
        result = 31 * result + (channelName != null ? channelName.hashCode() : 0);
        result = 31 * result + (streamTitle != null ? streamTitle.hashCode() : 0);
        result = 31 * result + (gameName != null ? gameName.hashCode() : 0);
        return result;
    }
}
