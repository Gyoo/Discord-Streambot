package ws.web.transformer.discord;

import net.dv8tion.jda.entities.TextChannel;
import ws.web.dto.discord.DiscordChannelDTO;
import ws.web.transformer.GenericTransformer;

/**
 * Created by Gyoo on 27/06/2016.
 */
public class DiscordChannelTransformer extends GenericTransformer<TextChannel, DiscordChannelDTO> {
    @Override
    public DiscordChannelDTO modelToDto(TextChannel model) {
        DiscordChannelDTO dto = null;
        if(model != null){
            dto = new DiscordChannelDTO();
            dto.setId(model.getId());
            dto.setGuildId(model.getGuild().getId());
            dto.setName(model.getName());
        }
        return dto;
    }

    @Override
    public TextChannel dtoToModel(DiscordChannelDTO discordChannelDTO) {
        return null;
    }
}
