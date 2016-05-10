package ws.discord.messages;

import entity.local.MessageItem;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageHandler {

    private static final LinkedBlockingQueue<MessageItem> queue = new LinkedBlockingQueue<>();
    private static MessageHandler ourInstance = new MessageHandler();

    public static MessageHandler getInstance() {
        return ourInstance;
    }

    private MessageHandler() {
    }

    public static LinkedBlockingQueue<MessageItem> getQueue() {
        return queue;
    }

    public void addToQueue(Long _id, MessageItem.Type _type, Message _message){
        String message = _message.getRawContent();
        List<String> messages = new ArrayList<>();
        while(message.length()>2000)
        {
            int index = message.lastIndexOf("\n",2000);
            if(index==-1)
                index = message.lastIndexOf(" ",2000);
            if(index==-1)
                index=2000;
            messages.add(message.substring(0,index));
            message = message.substring(index);
        }
        messages.add(message);
        for(String cutMessage : messages){
            MessageBuilder builder = new MessageBuilder();
            MessageItem messageItem = new MessageItem(Long.toString(_id), _type,builder.appendString(cutMessage).build());
            synchronized (queue){
                queue.add(messageItem);
                queue.notify();
            }
        }
    }

    public void addToQueue(String _id, MessageItem.Type _type, Message _message){
        addToQueue(Long.parseLong(_id), _type, _message);
    }
}
