package old;

import java.util.HashMap;
import java.util.Map;

public class Permissions {

    public static final int USE = 0;
    public static final int QUEUE = 1;
    public static final int FORBID = 2;

    Map<String, Integer> perms = new HashMap<>();

    public Permissions(){

    }

    public void addPermission(String r, int level){
        perms.put(r, level);
    }

    public Map<String, Integer> getPerms() {
        return perms;
    }

    public static String getPermissionLevel(int i){
        switch(i){
            case USE :
                return "Use";
            case QUEUE :
                return "Queue";
            case FORBID :
                return "Forbid";
            default :
                return "Unknown";
        }
    }
}
