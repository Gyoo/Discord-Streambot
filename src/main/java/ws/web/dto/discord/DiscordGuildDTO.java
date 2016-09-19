package ws.web.dto.discord;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class DiscordGuildDTO {

    private String id;
    private String name;
    private String iconURL;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }
}
