package ws.web;

import com.google.gson.Gson;
import common.api.twitch.Twitch;
import dao.Dao;
import entity.GuildEntity;
import net.dv8tion.jda.JDA;
import ws.web.dto.*;
import ws.web.dto.discord.DiscordGuildDTO;
import ws.web.dto.discord.UserDTO;
import ws.web.service.DiscordService;
import ws.web.service.GuildService;

import java.util.List;

import static spark.Spark.*;

public class WebController {

    private JDA jda;
    private Gson gson;
    private Dao dao;

    public WebController(JDA jda, Dao dao) {
        this.jda = jda;
        this.gson = new Gson();
        this.dao = dao;
    }

    public void setJda(JDA jda) {
        this.jda = jda;
    }

    public void serve(){

        get("/servers/:userID", (request, response) -> {
            response.status(200);
            response.type("application/json");
            response.header("Access-Control-Allow-Origin", "*");
            final List<DiscordGuildDTO> guilds = DiscordService.getGuildsForUser(request.params(":userID"), jda);
            return gson.toJson(guilds);
        });

        get("/server/:serverID", (request, response) -> {
            response.status(200);
            response.type("application/json");
            response.header("Access-Control-Allow-Origin", "*");
            final GuildDTO guildDTO = GuildService.getGuild(request.params(":serverID"), jda, dao);
            return gson.toJson(guildDTO);
        });

        get("/server/:serverID/streams", (request, response) -> {
            response.status(200);
            response.type("application/json");
            response.header("Access-Control-Allow-Origin", "*");
            final List<StreamDTO> streamDTOs = GuildService.getStreams(request.params(":serverID"), dao);
            return gson.toJson(streamDTOs);
        });

        get("/server/:serverID/channels", (request, response) -> {
            response.status(200);
            response.type("application/json");
            response.header("Access-Control-Allow-Origin", "*");
            final List<ChannelDTO> channelDTOs = GuildService.getChannels(request.params(":serverID"), dao);
            return gson.toJson(channelDTOs);
        });

        get("/server/:serverID/games", (request, response) -> {
            response.status(200);
            response.type("application/json");
            response.header("Access-Control-Allow-Origin", "*");
            final List<GameDTO> gameDTOs = GuildService.getGames(request.params(":serverID"), dao);
            return gson.toJson(gameDTOs);
        });

        get("/server/:serverID/teams", (request, response) -> {
            response.status(200);
            response.type("application/json");
            response.header("Access-Control-Allow-Origin", "*");
            final List<TeamDTO> teamDTOs = GuildService.getTeams(request.params(":serverID"), dao);
            return gson.toJson(teamDTOs);
        });

        get("/server/:serverID/managers", (request, response) -> {
            response.status(200);
            response.type("application/json");
            response.header("Access-Control-Allow-Origin", "*");
            final List<UserDTO> managerDTOs = GuildService.getManagers(request.params(":serverID"), jda, dao);
            return gson.toJson(managerDTOs);
        });

        get("/server/:serverID/tags", (request, response) -> {
            response.status(200);
            response.type("application/json");
            response.header("Access-Control-Allow-Origin", "*");
            final List<TagDTO> tagDTOs = GuildService.getTags(request.params(":serverID"), dao);
            return gson.toJson(tagDTOs);
        });
    }

}
