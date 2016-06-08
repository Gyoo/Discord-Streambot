package entity.local;

import entity.StreamEntity;
import net.dv8tion.jda.entities.Message;

public class MessageCreateAction extends MessageAction{

    public enum Type{
        GUILD,
        PRIVATE
    }

    private Message message;
    private String id;
    private Type type;
    private StreamEntity streamEntity;

    public MessageCreateAction(String _id, Type _type, Message _message, StreamEntity _stream){
        message = _message;
        type = _type;
        id = _id;
        streamEntity = _stream;
        action = Action.CREATE;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public StreamEntity getStreamEntity() {
        return streamEntity;
    }
}
