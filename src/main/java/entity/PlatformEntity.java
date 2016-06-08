package entity;

import javax.persistence.*;

/**
 * Created by Gyoo on 26/04/2016.
 */
@Entity
@Table(name = "platform", schema = "streambot", catalog = "")
public class PlatformEntity {
    private int platformId;
    private String name;

    @Id
    @Column(name = "PlatformID", nullable = false)
    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    @Basic
    @Column(name = "Name", nullable = true, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlatformEntity that = (PlatformEntity) o;

        if (platformId != that.platformId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = platformId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
