package entity;

import javax.persistence.*;

@Entity
@Table(name = "command", schema = "streambot", catalog = "")
public class CommandEntity {

    @Id
    @Column(name = "CommandID", nullable = false)
    private long commandId;

    @Basic
    @Column(name = "Name", nullable = false, length = -1)
    private String name;

    public long getCommandId() {
        return commandId;
    }

    public void setCommandId(long commandId) {
        this.commandId = commandId;
    }

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

        CommandEntity that = (CommandEntity) o;

        if (commandId != that.commandId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (commandId ^ (commandId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
