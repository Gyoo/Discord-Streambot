package ovh.gyoo.bot.listeners;

import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.MessageItem;

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
                        Thread.sleep(delta);
                        nbMessages = 0;
                    }
                }
                else{
                    start = System.currentTimeMillis();
                    nbMessages = 0;
                }

                /*switch(work.getType()){
                    case GUILD:
                        DiscordInstance.getInstance().getDiscord().getTextChannelById(work.getId()).sendMessage(work.getMessage());
                    case PRIVATE:
                        DiscordInstance.getInstance().getDiscord().getPrivateChannelById(work.getId()).sendMessage(work.getMessage());
                }*/ //Keep this commented while testing.
            }
            catch ( InterruptedException ie ) {
                break;  // Terminate
            }
        }
    }

}
