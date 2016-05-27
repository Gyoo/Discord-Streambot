package ws.discord.commands;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import common.CommonData;
import common.CommonOperations;
import entity.ManagerEntity;
import entity.QueueitemEntity;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.impl.MessageImpl;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ws.discord.messages.MessageHandler;

import java.util.ArrayList;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static org.testng.Assert.*;

public class CQueueTest extends CommandTest{

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        command = new CQueue(jda, dao);
    }

    private void initManager(){
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), CommonOperations.INSERT_MANAGER);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void testExecute() throws Exception {
        this.initManager();

        Operation operation =
                insertInto("queueitem")
                        .row()
                        .column("ServerID", 131483070464393216L)
                        .column("UserID", 180922097399365632L)
                        .column("Command", "Test")
                        .end()
                        .build();
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);

        List<QueueitemEntity> list = dao.getAll(QueueitemEntity.class);
        Assert.assertEquals(list.size(), 1);

        User user = jda.getUserById("180922097399365632");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "");

        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getContent(),
                "Commands queue for server StreamBot\nGyoo_Test : Test\n");

        list = dao.getAll(QueueitemEntity.class);
        Assert.assertEquals(list.size(), 0);

    }

    @Test
    public void testExecuteNotAllowed() throws Exception {
        User user = jda.getUserById("180922097399365632");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getContent(), "You are not allowed to use this command");
    }

}