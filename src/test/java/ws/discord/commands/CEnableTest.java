package ws.discord.commands;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import common.CommonData;
import common.CommonOperations;
import common.util.HibernateUtil;
import dao.Dao;
import entity.GuildEntity;
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
 * Created by Gyoo on 25/05/2016.
 */
public class CEnableTest extends CommandTest{

    @BeforeMethod
    @Override
    public void setUp() throws Exception {
        Operation operation = sequenceOf(
                CommonOperations.TRUNCATE_ALL,
                insertInto("guild")
                        .row()
                        .column("ServerID", 131483070464393216L)
                        .column("ChannelID", 131483070464393216L)
                        .column("isCompact", 0)
                        .column("isActive", 0)
                        .end()
                        .build()
        );
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), operation);
        dbSetup.launch();
        dao = new Dao();
        command = new CEnable(jda, dao);
    }

    private void initManager(){
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), CommonOperations.INSERT_MANAGER);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void testExecuteNotAllowed() throws Exception {
        GuildEntity guild = dao.getAll(GuildEntity.class).get(0);
        Assert.assertEquals(guild.isActive(), false);

        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "");
        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getContent(),
                "You are not allowed to use this command");

        HibernateUtil.getSession().refresh(guild);
        Assert.assertEquals(guild.isActive(), false);
    }

    @Test
    public void testExecuteEnable() throws Exception {
        this.initManager();
        GuildEntity guild = dao.getAll(GuildEntity.class).get(0);
        Assert.assertEquals(guild.isActive(), false);

        User user = jda.getUserById("180922097399365632");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "");
        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getContent(),
                "Bot enabled !");

        HibernateUtil.getSession().refresh(guild);
        Assert.assertEquals(guild.isActive(), true);
    }

}