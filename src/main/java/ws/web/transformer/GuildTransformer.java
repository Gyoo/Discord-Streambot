package ws.web.transformer;

import common.api.twitch.Twitch;
import entity.*;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;
import ws.web.dto.*;
import ws.web.dto.discord.DiscordChannelDTO;
import ws.web.dto.discord.DiscordGuildDTO;
import ws.web.dto.discord.UserDTO;
import ws.web.transformer.discord.DiscordChannelTransformer;
import ws.web.transformer.discord.DiscordGuildTransformer;

import java.util.ArrayList;
import java.util.List;

public class GuildTransformer extends GenericTransformer<GuildEntity, GuildDTO> {

    private QueueItemTransformer queueItemTransformer;
    private PermissionTransformer permissionTransformer;
    private NotificationTransformer notificationTransformer;
    private DiscordGuildTransformer discordGuildTransformer;
    private DiscordChannelTransformer discordChannelTransformer;
    private JDA jda;

    public GuildTransformer(JDA jda) {
        this.queueItemTransformer = new QueueItemTransformer(jda);
        this.permissionTransformer = new PermissionTransformer(jda);
        this.notificationTransformer = new NotificationTransformer(jda);
        this.discordGuildTransformer = new DiscordGuildTransformer();
        this.discordChannelTransformer = new DiscordChannelTransformer();
        this.jda = jda;
    }

    @Override
    public GuildDTO modelToDto(GuildEntity model) {
        GuildDTO dto = null;
        if(model != null){
            dto = new GuildDTO();

            final Guild guild = jda.getGuildById(Long.toString(model.getServerId()));
            final DiscordGuildDTO guildDTO = this.discordGuildTransformer.modelToDto(guild);
            dto.setServer(guildDTO);

            final TextChannel channel = jda.getTextChannelById(Long.toString(model.getChannelId()));
            final DiscordChannelDTO channelDTO = this.discordChannelTransformer.modelToDto(channel);
            dto.setChannel(channelDTO);
            dto.setActive(model.isActive());
            dto.setCompact(model.isCompact());
            dto.setCleanup(model.getCleanup());

            final List<QueueItemDTO> queueItems = this.queueItemTransformer.modelToDto(new ArrayList<QueueitemEntity>(model.getQueue()));
            dto.setQueue(queueItems);

            final List<UserDTO> notifications = this.notificationTransformer.modelToDto(new ArrayList<NotificationEntity>(model.getNotifications()));
            dto.setNotifications(notifications);

            final List<PermissionDTO> permissions = this.permissionTransformer.modelToDto(new ArrayList<PermissionEntity>(model.getPermissions()));
            dto.setPermissions(permissions);
        }
        return dto;
    }

    @Override
    public GuildEntity dtoToModel(GuildDTO guildDTO) {
        return null;
    }
}
