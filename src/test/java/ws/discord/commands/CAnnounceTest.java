package ws.discord.commands;

import dao.Dao;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.impl.MessageImpl;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ws.discord.messages.MessageHandler;

import static org.testng.Assert.*;

public class CAnnounceTest extends CommandTest{

    @BeforeMethod
    @Override
    public void setUp() throws Exception {
        dao = new Dao();
        command = new CAnnounce(jda, dao);
    }

    @Test
    public void testExecute() throws Exception {
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "test");
        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(),
                "test");
    }

}