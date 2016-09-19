package ws.web.transformer.discord;

import net.dv8tion.jda.entities.Guild;
import ws.web.dto.discord.DiscordGuildDTO;
import ws.web.transformer.GenericTransformer;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class DiscordGuildTransformer extends GenericTransformer<Guild, DiscordGuildDTO> {

    @Override
    public DiscordGuildDTO modelToDto(Guild model) {
        DiscordGuildDTO dto = null;
        if(model != null){
            dto = new DiscordGuildDTO();
            dto.setId(model.getId());
            dto.setName(model.getName());
            dto.setIconURL(model.getIconUrl());
        }
        return dto;
    }

    @Override
    public Guild dtoToModel(DiscordGuildDTO discordGuildDTO) {
        return null;
    }
}
