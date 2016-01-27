package ovh.gyoo.bot.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    // Currently unused, but I should use it

    public static void writeToLog(String s){
        BufferedWriter out = null;
        try
        {
            FileWriter fstream = new FileWriter("log.txt", true); //true tells to append data.
            out = new BufferedWriter(fstream);
            out.write(s);
            out.newLine();
        }
        catch (IOException e)
        {
            System.err.println("Error: " + e.getMessage());
        }
        finally
        {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeToErr(String s){
        BufferedWriter out = null;
        try
        {
            FileWriter fstream = new FileWriter("err.txt", true); //true tells to append data.
            out = new BufferedWriter(fstream);
            out.write(s);
            out.newLine();
        }
        catch (IOException e)
        {
            System.err.println("Error: " + e.getMessage());
        }
        finally
        {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
