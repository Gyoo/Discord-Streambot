package ws.discord.commands;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import common.CommonData;
import common.CommonOperations;
import dao.Dao;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.impl.MessageImpl;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ws.discord.messages.MessageHandler;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.testng.Assert.*;

/**
 * Created by Gyoo on 26/05/2016.
 */
public class CMoveTest extends CommandTest{

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        command = new CMove(jda, dao);
    }

    private void initManager(){
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), CommonOperations.INSERT_MANAGER);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void testExecuteNotAllowed() throws Exception {
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("test");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(), "You are not allowed to use this command");
    }

    @Test
    public void testExecute() throws Exception{
        this.initManager();
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("test");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "#bugs");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(), "Announces will now be done in #bugs !");
    }

}