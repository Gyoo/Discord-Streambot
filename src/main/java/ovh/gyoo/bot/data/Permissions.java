package ovh.gyoo.bot.data;

import net.dv8tion.jda.entities.Role;

import java.util.HashMap;
import java.util.Map;

public class Permissions {

    public static final int USE = 0;
    public static final int QUEUE = 1;
    public static final int FORBID = 2;

    Map<String, Integer> perms = new HashMap<>();

    public Permissions(){
        perms.put("StreambotManager", USE);
    }

    public boolean addPermission(String r, int level){
        if(!perms.containsKey(r))
        {
            perms.put(r, level);
            return true;
        }
        else return false;
    }

    public boolean removePermission(String r){
        if(perms.containsKey(r)) {
            perms.remove(r);
            return true;
        }
        else return false;
    }

    public Map<String, Integer> getPerms() {
        return perms;
    }
}
