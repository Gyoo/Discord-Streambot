package ws.web.dto.discord;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class UserDTO {

    private String id;
    private String username;
    private String avatarURL;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }
}
