package ws.web.service;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Guild;
import ws.web.dto.discord.DiscordGuildDTO;
import ws.web.transformer.discord.DiscordGuildTransformer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class DiscordService {

    public static List<DiscordGuildDTO> getGuildsForUser(String userID, JDA jda){
        final DiscordGuildTransformer transformer = new DiscordGuildTransformer();
        final List<Guild> guilds = jda.getGuilds().stream().filter(g -> g.getUsers().contains(jda.getUserById(userID))).collect(Collectors.toList());
        return transformer.modelToDto(guilds);
    }

}
