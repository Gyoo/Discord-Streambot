package ws.web.dto;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class TeamDTO {

    private String name;
    private Integer platformID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlatformID() {
        return platformID;
    }

    public void setPlatformID(Integer platformID) {
        this.platformID = platformID;
    }
}
