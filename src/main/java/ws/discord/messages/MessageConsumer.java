package ws.discord.messages;

import entity.local.MessageItem;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.exceptions.BlockedException;
import net.dv8tion.jda.exceptions.RateLimitedException;
import org.json.JSONException;

import java.util.concurrent.LinkedBlockingQueue;

public class MessageConsumer extends Thread {

    private final LinkedBlockingQueue<MessageItem> queue;
    private JDA jda;
    private long start = System.currentTimeMillis();
    private int nbMessages = 0;

    public MessageConsumer(LinkedBlockingQueue<MessageItem> queue, JDA jda) {
        this.queue = queue;
        this.jda = jda;
    }

    @Override
    public void run() {
        while (true) {
            try {
                MessageItem work;

                synchronized (queue) {
                    while (queue.isEmpty()) queue.wait();

                    // Get the next work item off of the queue
                    work = queue.remove();
                }

                /*switch (work.getType()) {
                    case GUILD:
                        User self = jda.getUserById(jda.getSelfInfo().getId());
                        TextChannel textChannel = jda.getTextChannelById(work.getId());
                        if (null != work.getMessage()
                                && null != textChannel
                                && null != jda.getTextChannelById(work.getId())
                                && jda.getTextChannelById(work.getId()).checkPermission(self, Permission.MESSAGE_WRITE)) {
                            try {
                                textChannel.sendMessage(work.getMessage());
                            } catch (NullPointerException e) {
                                //Logger.writeToErr(e, "Guild Channel id = " + work.getId());
                            } catch (RateLimitedException e) {
                                Thread.sleep(e.getTimeout());
                                MessageHandler.getInstance().addToQueue(Long.parseLong(work.getId()), work.getType(), work.getMessage());
                            } catch (JSONException e) {
                                //Logger.writeToErr(e, "[JSON Exception] : \n" + e.getLocalizedMessage());
                            }
                        }

                        break;
                    case PRIVATE:
                        try {
                            jda.getPrivateChannelById(work.getId()).sendMessage(work.getMessage());
                        } catch (NullPointerException | BlockedException e) {
                            //Logger.writeToErr(e, "Private Channel id = " + work.getId());
                        } catch (RateLimitedException e) {
                            Thread.sleep(e.getTimeout());
                            MessageHandler.getInstance().addToQueue(Long.parseLong(work.getId()), work.getType(), work.getMessage());
                        }
                        break;
                }*/ //Keep this switch commented while testing.
            } catch (InterruptedException ie) {
                continue;
            }
        }

    }
}
