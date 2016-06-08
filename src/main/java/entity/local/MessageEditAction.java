package entity.local;

import net.dv8tion.jda.entities.Message;

public class MessageEditAction extends MessageAction{

    private String id;
    private Message message;
    private String newString;

    public MessageEditAction(String id, Message message, String newString) {
        this.id = id;
        this.message = message;
        this.newString = newString;
        this.action = Action.EDIT;
    }

    public String getId() {
        return id;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    public String getNewString() {
        return newString;
    }
}
