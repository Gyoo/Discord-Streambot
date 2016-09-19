package ws.web.transformer.discord;

import net.dv8tion.jda.entities.Role;
import ws.web.dto.discord.RoleDTO;
import ws.web.transformer.GenericTransformer;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class RoleTransformer extends GenericTransformer<Role, RoleDTO> {


    @Override
    public RoleDTO modelToDto(Role model) {
        RoleDTO role = null;
        if(model != null){
            role = new RoleDTO();
            role.setName(model.getName());
            role.setColor(model.getColor());
        }
        return role;
    }

    @Override
    public Role dtoToModel(RoleDTO roleDTO) {
        return null;
    }
}
