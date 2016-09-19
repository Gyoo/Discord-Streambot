package ws.discord.commands;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import common.CommonData;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.impl.MessageImpl;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ws.discord.messages.MessageHandler;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static org.testng.Assert.*;

public class CStreamsTest extends CommandTest{

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        command = new CStreams(jda, dao);
    }

    @Test
    public void testExecuteNoStreams() throws Exception {
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(), "No streams online ! :(");
    }

    @Test
    public void testExecuteStreams() throws Exception {
        Operation operation =
                insertInto("stream")
                        .row()
                        .column("ServerID", 131483070464393216L)
                        .column("PlatformID", 1)
                        .column("Channel_name", "test")
                        .column("Stream_title", "test")
                        .column("Game_name", "test")
                        .end()
                        .build();
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(), "test playing test at ` http://twitch.tv/test ` : test\n");
    }

}