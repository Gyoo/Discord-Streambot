package ws.web.transformer;

import entity.QueueitemEntity;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.User;
import ws.web.dto.QueueItemDTO;
import ws.web.dto.discord.UserDTO;
import ws.web.transformer.discord.UserTransformer;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class QueueItemTransformer extends GenericTransformer<QueueitemEntity, QueueItemDTO> {

    private JDA jda;
    private UserTransformer userTransformer;

    public QueueItemTransformer(JDA jda) {
        this.jda = jda;
        this.userTransformer = new UserTransformer();
    }

    @Override
    public QueueItemDTO modelToDto(QueueitemEntity model) {
        QueueItemDTO dto = null;
        if(model != null){
            dto = new QueueItemDTO();
            dto.setCommand(model.getCommand());
            final User user = jda.getUserById(Long.toString(model.getUserId()));
            final UserDTO userDTO = this.userTransformer.modelToDto(user);
            dto.setUser(userDTO);
        }
        return dto;
    }

    @Override
    public QueueitemEntity dtoToModel(QueueItemDTO queueItemDTO) {
        return null;
    }
}
