package ovh.gyoo.bot.listeners;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.exceptions.BlockedException;
import net.dv8tion.jda.exceptions.RateLimitedException;
import org.json.JSONException;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.MessageItem;
import ovh.gyoo.bot.writer.Logger;

import java.time.LocalTime;
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
                switch(work.getType()){
                    case GUILD:
                        User self = DiscordInstance.getInstance().getDiscord().getUserById(DiscordInstance.getInstance().getDiscord().getSelfInfo().getId());
                        TextChannel textChannel = DiscordInstance.getInstance().getDiscord().getTextChannelById(work.getId());
                        if(null != work.getMessage()
                                && null != textChannel
                                && null != DiscordInstance.getInstance().getDiscord().getTextChannelById(work.getId())
                                && DiscordInstance.getInstance().getDiscord().getTextChannelById(work.getId()).checkPermission(self, Permission.MESSAGE_WRITE)){
                            try{
                                textChannel.sendMessage(work.getMessage());
                            }catch(NullPointerException e){
                                Logger.writeToErr(e, "Guild Channel id = " + work.getId());
                            }catch(RateLimitedException e){
                                Thread.sleep(e.getTimeout());
                                DiscordInstance.getInstance().addToQueue(work);
                            }catch (JSONException e){
                                Logger.writeToErr(e, "[JSON Exception] : \n" + e.getLocalizedMessage());
                            }
                        }

                        break;
                    case PRIVATE:
                        try{
                            DiscordInstance.getInstance().getDiscord().getPrivateChannelById(work.getId()).sendMessage(work.getMessage());
                        }catch(NullPointerException | BlockedException e){
                            Logger.writeToErr(e, "Private Channel id = " + work.getId());
                        }catch(RateLimitedException e){
                            Thread.sleep(e.getTimeout());
                            DiscordInstance.getInstance().addToQueue(work);
                        }
                        break;
                } //Keep this switch commented while testing.
            }
            catch ( InterruptedException ie ) {
                continue;
            }
        }
    }

}
