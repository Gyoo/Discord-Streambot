package ws.web.transformer;

import entity.ManagerEntity;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.User;
import ws.web.dto.discord.UserDTO;
import ws.web.transformer.discord.UserTransformer;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class ManagerTransformer extends GenericTransformer<ManagerEntity, UserDTO> {

    private JDA jda;
    private UserTransformer userTransformer;

    public ManagerTransformer(JDA jda) {
        this.jda = jda;
        this.userTransformer = new UserTransformer();
    }

    @Override
    public UserDTO modelToDto(ManagerEntity model) {
        UserDTO dto = null;
        if(model != null){
            final User user = jda.getUserById(Long.toString(model.getUserId()));
            dto = this.userTransformer.modelToDto(user);
        }
        return dto;
    }

    @Override
    public ManagerEntity dtoToModel(UserDTO userDTO) {
        return null;
    }
}
