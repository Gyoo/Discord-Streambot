package ovh.gyoo.bot.data;

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
}
