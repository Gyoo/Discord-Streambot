package entity;

import javax.persistence.*;

@Entity
@Table(name = "permission", schema = "streambot", catalog = "")
public class PermissionEntity {

    @Id
    @Column(name = "PermissionID", nullable = false)
    private long permissionId;

    @ManyToOne
    @JoinColumn(name = "ServerID", nullable = false)
    private GuildEntity guild;

    @Basic
    @Column(name = "RoleID", nullable = false)
    private long roleId;

    @OneToOne
    @JoinColumn(name = "CommandID", nullable = false)
    private CommandEntity command;

    @Basic
    @Column(name = "Level", nullable = false)
    private int level;


    public long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(long permissionId) {
        this.permissionId = permissionId;
    }

    public GuildEntity getGuild() {
        return guild;
    }

    public void setGuild(GuildEntity guild) {
        this.guild = guild;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public CommandEntity getCommand() {
        return command;
    }

    public void setCommand(CommandEntity command) {
        this.command = command;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionEntity that = (PermissionEntity) o;

        if (permissionId != that.permissionId) return false;
        if (roleId != that.roleId) return false;
        if (level != that.level) return false;
        if (guild != null ? !guild.equals(that.guild) : that.guild != null) return false;
        return command != null ? command.equals(that.command) : that.command == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (permissionId ^ (permissionId >>> 32));
        result = 31 * result + (guild != null ? guild.hashCode() : 0);
        result = 31 * result + (int) (roleId ^ (roleId >>> 32));
        result = 31 * result + (command != null ? command.hashCode() : 0);
        result = 31 * result + level;
        return result;
    }

}
