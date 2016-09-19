package ws.web.service;

import common.api.twitch.Twitch;
import dao.Dao;
import entity.*;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Game;
import ws.web.dto.*;
import ws.web.dto.discord.UserDTO;
import ws.web.transformer.*;
import ws.web.transformer.discord.UserTransformer;

import java.util.List;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class GuildService {

    public static GuildDTO getGuild(String serverID, JDA jda, Dao dao){
        final GuildEntity guildEntity = dao.getLongId(GuildEntity.class, serverID);
        GuildTransformer transformer = new GuildTransformer(jda);
        return transformer.modelToDto(guildEntity);
    }

    public static List<StreamDTO> getStreams(String serverID, Dao dao){
        final List<StreamEntity> entities = dao.getForGuild(StreamEntity.class, serverID);
        StreamTransformer transformer = new StreamTransformer();
        return transformer.modelToDto(entities);
    }

    public static List<ChannelDTO> getChannels(String serverID, Dao dao){
        final List<ChannelEntity> entities = dao.getForGuild(ChannelEntity.class, serverID);
        ChannelTransformer transformer = new ChannelTransformer();
        return transformer.modelToDto(entities);
    }

    public static List<GameDTO> getGames(String serverID, Dao dao){
        final List<GameEntity> entities = dao.getForGuild(GameEntity.class, serverID);
        GameTransformer transformer = new GameTransformer();
        return transformer.modelToDto(entities);
    }

    public static List<TeamDTO> getTeams(String serverID, Dao dao){
        final List<TeamEntity> entities = dao.getForGuild(TeamEntity.class, serverID);
        TeamTransformer transformer = new TeamTransformer();
        return transformer.modelToDto(entities);
    }

    public static List<UserDTO> getManagers(String serverID, JDA jda, Dao dao){
        final List<ManagerEntity> entities = dao.getForGuild(ManagerEntity.class, serverID);
        ManagerTransformer transformer = new ManagerTransformer(jda);
        return transformer.modelToDto(entities);
    }

    public static List<TagDTO> getTags(String serverID, Dao dao){
        final List<TagEntity> entities = dao.getForGuild(TagEntity.class, serverID);
        TagTransformer transformer = new TagTransformer();
        return transformer.modelToDto(entities);
    }

}
