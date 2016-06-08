package entity.local;

import net.dv8tion.jda.entities.Message;

public abstract class MessageAction {

    public enum Action{
        CREATE,
        EDIT,
        DELETE
    }

    public Action action;

    public abstract Message getMessage();

}
