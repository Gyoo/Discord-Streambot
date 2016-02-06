package ovh.gyoo.bot.data;

import net.dv8tion.jda.entities.Message;

public class MessageItem {

    public enum Type{
        GUILD,
        PRIVATE
    }

    private Message message;
    private String id;
    private Type type;

    public MessageItem(String _id, Type _type, Message _message){
        message = _message;
        type = _type;
        id = _id;
    }

    public Message getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }
}
