package ws.web.dto;

import ws.web.dto.discord.RoleDTO;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class PermissionDTO {

    private RoleDTO role;
    private String commandName;
    private Integer level;

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
