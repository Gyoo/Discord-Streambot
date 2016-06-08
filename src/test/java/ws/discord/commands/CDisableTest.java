package ws.discord.commands;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import common.CommonData;
import common.CommonOperations;
import common.util.HibernateUtil;
import dao.Dao;
import entity.GuildEntity;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.impl.MessageImpl;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.junit.Ignore;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ws.discord.messages.MessageHandler;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.testng.Assert.*;

public class CDisableTest extends CommandTest{

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        command = new CDisable(jda, dao);
    }

    public void initManager(){
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), CommonOperations.INSERT_MANAGER);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void testExecuteNotAllowed() throws Exception {
        GuildEntity guild = dao.getAll(GuildEntity.class).get(0);
        Assert.assertEquals(guild.isActive(), true);

        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "");
        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getContent(),
                "You are not allowed to use this command");

        HibernateUtil.getSession().refresh(guild);
        Assert.assertEquals(guild.isActive(), true);
    }

    @Test
    public void testExecuteDisable() throws Exception {
        this.initManager();
        GuildEntity guild = dao.getAll(GuildEntity.class).get(0);
        Assert.assertEquals(guild.isActive(), true);

        User user = jda.getUserById("180922097399365632");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "");
        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getContent(),
                "Bot disabled !");

        HibernateUtil.getSession().refresh(guild);
        Assert.assertEquals(guild.isActive(), false);
    }

}