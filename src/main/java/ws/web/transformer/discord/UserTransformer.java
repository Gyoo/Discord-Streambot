package ws.web.transformer.discord;

import net.dv8tion.jda.entities.User;
import ws.web.dto.discord.UserDTO;
import ws.web.transformer.GenericTransformer;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class UserTransformer extends GenericTransformer<User, UserDTO> {
    @Override
    public UserDTO modelToDto(User model) {
        UserDTO dto = null;
        if(model != null){
            dto = new UserDTO();
            dto.setId(model.getId());
            dto.setAvatarURL(model.getAvatarUrl());
            dto.setUsername(model.getUsername());
        }
        return dto;
    }

    @Override
    public User dtoToModel(UserDTO userDTO) {
        return null;
    }
}
