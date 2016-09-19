package ws.discord.commands;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import common.CommonData;
import common.CommonOperations;
import common.util.HibernateUtil;
import entity.NotificationEntity;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.impl.MessageImpl;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.hibernate.Hibernate;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ws.discord.messages.MessageHandler;

import java.util.List;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static org.testng.Assert.*;

public class CNotifyTest extends CommandTest{

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        command = new CNotify(jda, dao);
    }

    private void initManager(){
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), CommonOperations.INSERT_MANAGER);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void testExecuteNotAllowedEveryone() throws Exception {
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "everyone");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(), "You are not allowed to use this command");
    }

    @Test
    public void testExecuteNotAllowedHere() throws Exception {
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "here");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(), "You are not allowed to use this command");
    }

    @Test
    public void testExecuteMe() throws Exception {
        List<NotificationEntity> list = dao.getAll(NotificationEntity.class);
        Assert.assertEquals(list.size(), 0);
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "me");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(), "You will be mentioned in announces !");
        list = dao.getAll(NotificationEntity.class);
        Assert.assertEquals(list.size(), 1);
    }

    @Test
    public void testExecuteUnknowParam() throws Exception {
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(), "Unknown parameter");
    }

    @Test
    public void testExecuteMeNot() throws Exception {
        Operation operation =
                insertInto("notification")
                        .row()
                        .column("ServerID", 131483070464393216L)
                        .column("UserID", 63263941735755776L)
                        .end()
                        .build();
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
        List<NotificationEntity> list = dao.getAll(NotificationEntity.class);
        Assert.assertEquals(list.size(), 1);
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "me");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(), "You will not be mentioned in announces anymore !");
        list = dao.getAll(NotificationEntity.class);
        Assert.assertEquals(list.size(), 0);
    }

    @Test
    public void testExecuteEveryone() throws Exception {
        this.initManager();
        List<NotificationEntity> list = dao.getAll(NotificationEntity.class);
        Assert.assertEquals(list.size(), 0);
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "everyone");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(), "Announces will mention \"everyone\" !");
        list = dao.getAll(NotificationEntity.class);
        Assert.assertEquals(list.size(), 1);
    }

    @Test
    public void testExecuteEveryoneNot() throws Exception {
        this.initManager();
        Operation operation =
                insertInto("notification")
                        .row()
                        .column("ServerID", 131483070464393216L)
                        .column("UserID", 0L)
                        .end()
                        .build();
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
        List<NotificationEntity> list = dao.getAll(NotificationEntity.class);
        Assert.assertEquals(list.size(), 1);
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "everyone");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(), "Removed \"everyone\" from announces mentions !");
        list = dao.getAll(NotificationEntity.class);
        Assert.assertEquals(list.size(), 0);
    }

    @Test
    public void testExecuteHere() throws Exception {
        this.initManager();
        List<NotificationEntity> list = dao.getAll(NotificationEntity.class);
        Assert.assertEquals(list.size(), 0);
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "here");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(), "Announces will mention \"here\" !");
        list = dao.getAll(NotificationEntity.class);
        Assert.assertEquals(list.size(), 1);
    }

    @Test
    public void testExecuteHereNot() throws Exception {
        this.initManager();
        Operation operation =
                insertInto("notification")
                        .row()
                        .column("ServerID", 131483070464393216L)
                        .column("UserID", 1L)
                        .end()
                        .build();
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
        List<NotificationEntity> list = dao.getAll(NotificationEntity.class);
        Assert.assertEquals(list.size(), 1);
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "here");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(), "Removed \"here\" from announces mentions !");
        list = dao.getAll(NotificationEntity.class);
        Assert.assertEquals(list.size(), 0);
    }

}