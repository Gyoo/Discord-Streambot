package ws.discord.commands;

import dao.Dao;
import entity.CommandEntity;
import entity.GuildEntity;
import entity.ManagerEntity;
import entity.PermissionEntity;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    protected enum Allowances{
        ALL,
        PERMISSIONS,
        MANAGERS,
        ADMIN
    }

    protected Dao dao;
    protected JDA jda;
    protected String description;
    public List<Allowances> allows = new ArrayList<>();

    public Command(JDA jda, Dao dao){
        this.dao = dao;
        this.jda = jda;
    }

    abstract public void execute(MessageReceivedEvent e, String content);

    public String getDescription(){
        return description;
    }

    public boolean isAllowed(String serverID, String authorID, List<Allowances> allowances, int level, CommandEntity command){
        boolean allowed = false;
        GuildEntity guild = null;
        if(serverID != null && !serverID.equals("")){
            guild = dao.getLongId(GuildEntity.class, serverID);
        }
        if(guild != null || allowances.contains(Allowances.ADMIN)){
            for(Allowances allow : allowances){
                switch (allow){
                    case ALL:
                        allowed = true;
                        break;
                    case ADMIN:
                        allowed = authorID.equals("63263941735755776");
                        break;
                    case MANAGERS:
                        assert guild != null;
                        for(ManagerEntity manager : guild.getManagers()){
                            if(manager.getUserId() == Long.parseLong(authorID)){
                                allowed = true;
                                break;
                            }
                        }
                        break;
                    case PERMISSIONS:
                        allowed = getPermissionsLevel(guild, authorID, command) <= level;
                        break;
                }
                if(allowed) break;
            }
        }
        return allowed;
    }

    protected int getPermissionsLevel(GuildEntity guild, String authorID, CommandEntity command){
        User user = jda.getUserById(authorID);
        List<Role> roles = jda.getGuildById(Long.toString(guild.getServerId())).getRolesForUser(user);
        for(ManagerEntity manager : guild.getManagers()){
            if(manager.getUserId() == Long.parseLong(authorID)){
                return 0;
            }
        }
        int level = 2;
        if(command != null){
            for(PermissionEntity permission : guild.getPermissions()){
                if(permission.getCommand().getCommandId() == command.getCommandId()){
                    if(permission.getRoleId() == 0L){
                        level = Math.min(level, permission.getLevel());
                    }
                    else{
                        for(Role role : roles){
                            if (permission.getRoleId() == Long.parseLong(role.getId())){
                                level = Math.min(level, permission.getLevel());
                            }
                        }
                    }
                }
            }
        }
        return level;
    }

    protected int getPermissionsLevel(String serverID, String authorID, CommandEntity command){
        GuildEntity guild = dao.getLongId(GuildEntity.class, serverID);
        return getPermissionsLevel(guild, authorID, command);
    }

    public CommandEntity getCommandEntity(){
        return null;
    }

}
