package ws.web.transformer;

import entity.PermissionEntity;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Role;
import ws.web.dto.PermissionDTO;
import ws.web.dto.discord.RoleDTO;
import ws.web.transformer.discord.RoleTransformer;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class PermissionTransformer extends GenericTransformer<PermissionEntity, PermissionDTO> {

    private JDA jda;
    private RoleTransformer roleTransformer;

    public PermissionTransformer(JDA jda) {
        this.jda = jda;
        this.roleTransformer = new RoleTransformer();
    }

    @Override
    public PermissionDTO modelToDto(PermissionEntity model) {
        PermissionDTO dto = null;
        if(model != null){
            dto = new PermissionDTO();
            if(model.getRoleId() != 0){
                final Role role = jda.getGuildById(Long.toString(model.getGuild().getServerId())).getRoleById(Long.toString(model.getRoleId()));
                final RoleDTO roleDTO = this.roleTransformer.modelToDto(role);
                dto.setRole(roleDTO);
            }
            else{
                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setName("Everyone");
                roleDTO.setColor(10070709);
                dto.setRole(roleDTO);
            }
            dto.setCommandName(model.getCommand().getName());
            dto.setLevel(model.getLevel());
        }
        return dto;
    }

    @Override
    public PermissionEntity dtoToModel(PermissionDTO permissionDTO) {
        return null;
    }
}
