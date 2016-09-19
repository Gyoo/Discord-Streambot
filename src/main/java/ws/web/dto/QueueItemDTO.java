package ws.web.dto;

import ws.web.dto.discord.UserDTO;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class QueueItemDTO {

    private String command;
    private UserDTO user;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
