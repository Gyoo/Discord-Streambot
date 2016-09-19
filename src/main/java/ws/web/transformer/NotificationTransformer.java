package ws.web.transformer;

import entity.NotificationEntity;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.User;
import ws.web.dto.discord.UserDTO;
import ws.web.transformer.discord.UserTransformer;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class NotificationTransformer extends GenericTransformer<NotificationEntity, UserDTO> {

    private JDA jda;
    private UserTransformer userTransformer;

    public NotificationTransformer(final JDA jda) {
        this.jda = jda;
        this.userTransformer = new UserTransformer();
    }

    @Override
    public UserDTO modelToDto(NotificationEntity model) {
        UserDTO dto = null;
        if(model != null){
            switch(Long.toString(model.getUserId())){
                case "0":
                    dto = new UserDTO();
                    dto.setUsername("@everyone");
                    break;
                case "1":
                    dto = new UserDTO();
                    dto.setUsername("@here");
                    break;
                default:
                    final User user = jda.getUserById(Long.toString(model.getUserId()));
                    dto = this.userTransformer.modelToDto(user);
                    break;
            }
        }
        return dto;
    }

    @Override
    public NotificationEntity dtoToModel(UserDTO userDTO) {
        return null;
    }
}
