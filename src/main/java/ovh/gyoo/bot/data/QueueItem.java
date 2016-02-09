package ovh.gyoo.bot.data;

public class QueueItem {

    String author;
    String command;

    public QueueItem(String auth, String comm){
        author = auth;
        command = comm;
    }

    public String getAuthor() {
        return author;
    }

    public String getCommand() {
        return command;
    }
}
