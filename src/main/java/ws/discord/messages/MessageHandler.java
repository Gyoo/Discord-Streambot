package ws.discord.messages;

import entity.StreamEntity;
import entity.local.MessageAction;
import entity.local.MessageCreateAction;
import entity.local.MessageDeleteAction;
import entity.local.MessageEditAction;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageHandler {

    private static final LinkedBlockingQueue<MessageAction> queue = new LinkedBlockingQueue<>();
    private static final LinkedBlockingQueue<StreamEntity> deletequeue = new LinkedBlockingQueue<>();
    private static MessageHandler ourInstance = new MessageHandler();

    public static MessageHandler getInstance() {
        return ourInstance;
    }

    private MessageHandler() {
    }

    public static LinkedBlockingQueue<MessageAction> getQueue() {
        return queue;
    }

    public static LinkedBlockingQueue<StreamEntity> getDeleteQueue() {
        return deletequeue;
    }

    public void addCreateToQueue(Long _id, MessageCreateAction.Type _type, Message _message, StreamEntity... _stream){
        this.addCreateToQueue(Long.toString(_id), _type, _message, _stream);
    }

    public void addCreateToQueue(String _id, MessageCreateAction.Type _type, Message _message, StreamEntity... _stream){
        String message = _message.getRawContent();
        List<String> messages = new ArrayList<>();
        while(message.length()>2000)
        {
            int index = message.lastIndexOf("\n",1999);
            if(index==-1)
                index = message.lastIndexOf(" ",1999);
            if(index==-1)
                index=1999;
            messages.add(message.substring(0,index));
            message = message.substring(index);
        }
        messages.add(message);
        for(String cutMessage : messages){
            MessageBuilder builder = new MessageBuilder();
            MessageCreateAction messageItem;
            if(_stream.length > 0){
                messageItem = new MessageCreateAction(_id, _type, builder.appendString(cutMessage).build(), _stream[0]);
            }
            else {
                messageItem = new MessageCreateAction(_id, _type, builder.appendString(cutMessage).build(), null);
            }
            synchronized (queue){
                queue.add(messageItem);
                queue.notify();
            }
        }
    }

    public void addEditToQueue(Long id, Message message, String newMessage){
        this.addEditToQueue(Long.toString(id), message, newMessage);
    }

    public void addEditToQueue(String id, Message message, String newMessage){
        if(newMessage.length() < 2000){
            MessageEditAction messageEditAction = new MessageEditAction(id, message, newMessage);
            synchronized (queue){
                queue.add(messageEditAction);
                queue.notify();
            }
        }
    }

    public void addDeleteToQueue(Long id, Message message){
        this.addDeleteToQueue(Long.toString(id), message);
    }

    public void addDeleteToQueue(String id, Message message){
            MessageDeleteAction messageDeleteAction = new MessageDeleteAction(id, message);
            synchronized (queue){
                queue.add(messageDeleteAction);
                queue.notify();
            }
    }

    public void addStreamToDeleteQueue(StreamEntity streamEntity){
        synchronized (deletequeue){
            deletequeue.add(streamEntity);
            deletequeue.notify();
        }
    }

}
