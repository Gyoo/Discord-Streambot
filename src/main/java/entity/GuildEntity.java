package entity;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;


@Entity
@Table(name = "guild", schema = "streambot", catalog = "")
public class GuildEntity {

    @Id
    @Column(name = "ServerID", nullable = false)
    private long serverId;

    @Basic
    @Column(name = "ChannelID", nullable = false)
    private long channelId;

    @Basic
    @Column(name = "isCompact", nullable = false)
    private boolean isCompact;

    @Basic
    @Column(name = "isActive")
    private boolean isActive;

    @OneToMany(mappedBy = "guild", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<ChannelEntity> channels;

    @OneToMany(mappedBy = "guild", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<TagEntity> tags;

    @OneToMany(mappedBy = "guild", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<TeamEntity> teams;

    @OneToMany(mappedBy = "guild", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<GameEntity> games;

    @OneToMany(mappedBy = "guild", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<StreamEntity> streams;

    @OneToMany(mappedBy = "guild", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<QueueitemEntity> queue;

    @OneToMany(mappedBy = "guild", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<ManagerEntity> managers;

    @OneToMany(mappedBy = "guild", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<NotificationEntity> notifications;

    @OneToMany(mappedBy = "guild", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<PermissionEntity> permissions;

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public boolean isCompact() {
        return isCompact;
    }

    public void setCompact(boolean compact) {
        isCompact = compact;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<ChannelEntity> getChannels() {
        return channels;
    }

    public void setChannels(Set<ChannelEntity> channels) {
        this.channels = channels;
    }

    public Set<TagEntity> getTags() {
        return tags;
    }

    public void setTags(Set<TagEntity> tags) {
        this.tags = tags;
    }

    public Set<TeamEntity> getTeams() {
        return teams;
    }

    public void setTeams(Set<TeamEntity> teams) {
        this.teams = teams;
    }

    public Set<GameEntity> getGames() {
        return games;
    }

    public void setGames(Set<GameEntity> games) {
        this.games = games;
    }

    public Set<StreamEntity> getStreams() {
        return streams;
    }

    public void setStreams(Set<StreamEntity> streams) {
        this.streams = streams;
    }

    public Set<QueueitemEntity> getQueue() {
        return queue;
    }

    public void setQueue(Set<QueueitemEntity> queue) {
        this.queue = queue;
    }

    public Set<ManagerEntity> getManagers() {
        return managers;
    }

    public void setManagers(Set<ManagerEntity> managers) {
        this.managers = managers;
    }

    public Set<NotificationEntity> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<NotificationEntity> notifications) {
        this.notifications = notifications;
    }

    public Set<PermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionEntity> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuildEntity that = (GuildEntity) o;

        if (serverId != that.serverId) return false;
        if (channelId != that.channelId) return false;
        if (isCompact != that.isCompact) return false;
        if (isActive != that.isActive) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = (int) (serverId ^ (serverId >>> 32));
        result = 31 * result + (int) (channelId ^ (channelId >>> 32));
        result = 31 * result + (isCompact ? 1 : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }
}
