package entity.local;

import net.dv8tion.jda.entities.Message;

public class MessageDeleteAction extends MessageAction{

    private String id;
    private Message message;

    public MessageDeleteAction(String id, Message message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    @Override
    public Message getMessage() {
        return message;
    }
}
