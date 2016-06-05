package common;

import java.time.LocalTime;

public class Logger {

    public static void writeToErr(Exception e, String message){
        System.err.print("["+ LocalTime.now().toString() +"] [StreamBot] ");
        e.printStackTrace();
        if(!message.isEmpty()) System.err.println("["+ LocalTime.now().toString() +"] [StreamBot] " + message);
    }

}
