package ws.discord.messages;

import common.Logger;
import common.PropertiesReader;
import dao.Dao;
import entity.StreamEntity;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.exceptions.PermissionException;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageFinder extends Thread {

    private final LinkedBlockingQueue<StreamEntity> queue;
    private JDA jda;

    public MessageFinder(LinkedBlockingQueue<StreamEntity> queue, JDA jda) {
        this.queue = queue;
        this.jda = jda;
    }

    @Override
    public void run() {
        while (true) {
            StreamEntity work = null;
            try {
                synchronized (queue) {
                    while (queue.isEmpty()) queue.wait();

                    // Get the next work item off of the queue
                    work = queue.remove();
                }

                if (Boolean.parseBoolean(PropertiesReader.getInstance().getProp().getProperty("mode.debug"))) {
                    continue;
                }

                MessageHistory history = new MessageHistory(jda.getTextChannelById(Long.toString(work.getGuild().getChannelId())));
                List<Message> messages = history.retrieve(100);
                for (Message message : messages) {
                    if (message.getId().equals(Long.toString(work.getMessageId()))) {
                        switch (work.getGuild().getCleanup()) {
                            case 1:
                                String content = message.getRawContent().substring(message.getRawContent().indexOf(":"));
                                MessageHandler.getInstance().addEditToQueue(work.getGuild().getChannelId(), message, "OFFLINE :" + content);
                                break;
                            case 2:
                                MessageHandler.getInstance().addDeleteToQueue(work.getGuild().getChannelId(), message);
                                break;
                            case 0:
                            default:
                                break;
                        }
                        break;
                    }
                }

            } catch(PermissionException pe){
                assert work != null;
                Logger.writeToErr(pe, "Guild : " + jda.getGuildById(Long.toString(work.getGuild().getServerId())).getName());
            } catch (InterruptedException ie) {
                continue;
            } catch(RuntimeException e){
                Logger.writeToErr(e, "");
            }
        }

    }
}