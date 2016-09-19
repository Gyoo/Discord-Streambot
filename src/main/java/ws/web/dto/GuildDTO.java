package ws.web.dto;

import ws.web.dto.discord.DiscordChannelDTO;
import ws.web.dto.discord.DiscordGuildDTO;
import ws.web.dto.discord.UserDTO;

import java.util.List;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class GuildDTO {

    private DiscordGuildDTO server;
    private DiscordChannelDTO channel;
    private Boolean isCompact;
    private Boolean isActive;
    private Integer cleanup;
    private List<QueueItemDTO> queue;
    private List<UserDTO> notifications;
    private List<PermissionDTO> permissions;

    public DiscordGuildDTO getServer() {
        return server;
    }

    public void setServer(DiscordGuildDTO server) {
        this.server = server;
    }

    public DiscordChannelDTO getChannel() {
        return channel;
    }

    public void setChannel(DiscordChannelDTO channel) {
        this.channel = channel;
    }

    public Boolean getCompact() {
        return isCompact;
    }

    public void setCompact(Boolean compact) {
        isCompact = compact;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getCleanup() {
        return cleanup;
    }

    public void setCleanup(Integer cleanup) {
        this.cleanup = cleanup;
    }

    public List<QueueItemDTO> getQueue() {
        return queue;
    }

    public void setQueue(List<QueueItemDTO> queue) {
        this.queue = queue;
    }

    public List<UserDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<UserDTO> notifications) {
        this.notifications = notifications;
    }

    public List<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDTO> permissions) {
        this.permissions = permissions;
    }
}
