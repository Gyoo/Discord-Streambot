package ws.discord.messages;

import common.Logger;
import common.PropertiesReader;
import dao.Dao;
import entity.PermissionEntity;
import entity.StreamEntity;
import entity.local.MessageAction;
import entity.local.MessageCreateAction;
import entity.local.MessageDeleteAction;
import entity.local.MessageEditAction;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.exceptions.BlockedException;
import net.dv8tion.jda.exceptions.RateLimitedException;
import net.dv8tion.jda.exceptions.VerificationLevelException;
import org.json.JSONException;

import java.util.concurrent.LinkedBlockingQueue;

public class MessageConsumer extends Thread {

    private final LinkedBlockingQueue<MessageAction> queue;
    private JDA jda;
    private Dao dao;

    public MessageConsumer(LinkedBlockingQueue<MessageAction> queue, JDA jda, Dao dao) {
        this.queue = queue;
        this.jda = jda;
        this.dao = dao;
    }

    @Override
    public void run() {
        while (true) {
            try {
                MessageAction work;

                synchronized (queue) {
                    while (queue.isEmpty()) queue.wait();

                    // Get the next work item off of the queue
                    work = queue.remove();
                }

                if (Boolean.parseBoolean(PropertiesReader.getInstance().getProp().getProperty("mode.debug"))){
                    continue;
                }

                if(work != null){
                    switch (work.action) {
                        case CREATE:
                            this.createMessage((MessageCreateAction) work);
                            break;
                        case EDIT:
                            this.editMessage((MessageEditAction) work);
                            break;
                        case DELETE:
                            this.deleteMessage((MessageDeleteAction) work);
                            break;
                    }
                }
            } catch (InterruptedException ie) {
                continue;
            }
        }

    }

    private void createMessage(MessageCreateAction work) throws InterruptedException {
        switch (work.getType()) {
            case GUILD:
                User self = jda.getUserById(jda.getSelfInfo().getId());
                TextChannel textChannel = jda.getTextChannelById(work.getId());
                if (null != work.getMessage()
                        && null != textChannel
                        && null != jda.getTextChannelById(work.getId())
                        && jda.getTextChannelById(work.getId()).checkPermission(self, Permission.MESSAGE_WRITE)) {
                    try {
                        Message message = textChannel.sendMessage(work.getMessage());
                        if (work.getStreamEntity() != null) {
                            StreamEntity streamEntity = work.getStreamEntity();
                            streamEntity.setMessageId(Long.parseLong(message.getId()));
                            dao.saveOrUpdate(streamEntity);
                        }
                    } catch (NullPointerException e) {
                        Logger.writeToErr(e, "Guild Channel id = " + work.getId());
                    } catch (RateLimitedException e) {
                        Thread.sleep(e.getTimeout());
                        MessageHandler.getInstance().addCreateToQueue(Long.parseLong(work.getId()), work.getType(), work.getMessage());
                    } catch (JSONException e) {
                        Logger.writeToErr(e, "[JSON Exception] : " + e.getLocalizedMessage());
                    } catch (VerificationLevelException e){
                        Logger.writeToErr(e, "Guild : " + jda.getGuildById(Long.toString(work.getStreamEntity().getGuild().getServerId())).getName());
                    }
                }

                break;
            case PRIVATE:
                try {
                    jda.getPrivateChannelById(work.getId()).sendMessage(work.getMessage());
                } catch (NullPointerException | BlockedException e) {
                    Logger.writeToErr(e, "Private Channel id = " + work.getId());
                } catch (RateLimitedException e) {
                    Thread.sleep(e.getTimeout());
                    MessageHandler.getInstance().addCreateToQueue(Long.parseLong(work.getId()), work.getType(), work.getMessage());
                }
                break;
        }
    }

    private void editMessage(MessageEditAction work) {
        User self = jda.getUserById(jda.getSelfInfo().getId());
        if (null != work.getMessage()
                && jda.getTextChannelById(work.getId()).checkPermission(self, Permission.MESSAGE_MANAGE)) {
            Message message = work.getMessage();
            message.updateMessage(work.getNewString());
        }
    }

    private void deleteMessage(MessageDeleteAction work){
        User self = jda.getUserById(jda.getSelfInfo().getId());
        if (null != work.getMessage()
                && jda.getTextChannelById(work.getId()).checkPermission(self, Permission.MESSAGE_MANAGE)) {
            work.getMessage().deleteMessage();
        }
    }
}
