package ovh.gyoo.bot.listeners;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.User;
import org.json.JSONException;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.MessageItem;
import ovh.gyoo.bot.writer.Logger;

import java.util.concurrent.LinkedBlockingQueue;

public class MessageConsumer extends Thread {

    private final LinkedBlockingQueue<MessageItem> queue;
    private long start = System.currentTimeMillis();
    private int nbMessages = 0;

    public MessageConsumer(LinkedBlockingQueue<MessageItem> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        while ( true ) {
            try {
                MessageItem work;

                synchronized ( queue ) {
                    while ( queue.isEmpty() ) queue.wait();

                    // Get the next work item off of the queue
                    work = queue.remove();
                }

                long delta = System.currentTimeMillis() - start;
                if(delta/1000.0<10.0){
                    nbMessages++;
                    if(nbMessages > 9) {
                        Thread.sleep(10000);
                        nbMessages = 0;
                    }
                }
                else{
                    start = System.currentTimeMillis();
                    nbMessages = 0;
                }

                try{
                    switch(work.getType()){
                        case GUILD:
                            User self = DiscordInstance.getInstance().getDiscord().getUserById(DiscordInstance.getInstance().getDiscord().getSelfInfo().getId());
                            if(null != DiscordInstance.getInstance().getDiscord().getTextChannelById(work.getId())
                            && DiscordInstance.getInstance().getDiscord().getTextChannelById(work.getId()).checkPermission(self, Permission.MESSAGE_WRITE)){
                                try{
                                    DiscordInstance.getInstance().getDiscord().getTextChannelById(work.getId()).sendMessage(work.getMessage());
                                }catch(NullPointerException e){
                                    System.err.print("[StreamBot] ");
                                    e.printStackTrace();
                                    System.err.println("Guild Channel id = " + work.getId());
                                }
                            }

                            break;
                        case PRIVATE:
                            try{
                                DiscordInstance.getInstance().getDiscord().getPrivateChannelById(work.getId()).sendMessage(work.getMessage());
                            }catch(NullPointerException e){
                                System.err.print("[StreamBot] ");
                                e.printStackTrace();
                                System.err.println("Private Channel id = " + work.getId());
                            }
                            break;
                    } //Keep this switch commented while testing.
                }catch(JSONException e){
                    Logger.writeToErr(e.getMessage());
                }

            }
            catch ( InterruptedException ie ) {
                continue;
            }
        }
    }

}
