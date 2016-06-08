package ws.discord.commands;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import common.CommonData;
import common.CommonOperations;
import dao.Dao;
import entity.GuildEntity;
import net.dv8tion.jda.entities.Guild;
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

public class CListTest extends CommandTest{

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        command = new CList(jda, dao);
    }

    private void initManager(){
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), CommonOperations.INSERT_MANAGER);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void testListGame() throws Exception {
        Operation operation = insertInto("game")
                .row()
                    .column("ServerID", 131483070464393216L)
                    .column("Name", "Test")
                    .column("PlatformID", 1)
                .end()
                .build();
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), operation);
        dbSetup.launch();

        GuildEntity guild = dao.getAll(GuildEntity.class).get(0);
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "game");
        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getContent(),
                "List of games for server **"+ jda.getGuildById(Long.toString(guild.getServerId())).getName() + "**\nTest\n");
    }

    @Test
    public void testListChannel() throws Exception {
        Operation operation = insertInto("channel")
                .row()
                .column("ServerID", 131483070464393216L)
                .column("Name", "Test")
                .column("PlatformID", 1)
                .end()
                .build();
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), operation);
        dbSetup.launch();

        GuildEntity guild = dao.getAll(GuildEntity.class).get(0);
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "channel");
        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getContent(),
                "List of channels for server **"+ jda.getGuildById(Long.toString(guild.getServerId())).getName() + "**\nTest\n");
    }

    @Test
    public void testListTag() throws Exception {
        Operation operation = insertInto("tag")
                .row()
                .column("ServerID", 131483070464393216L)
                .column("Name", "Test")
                .end()
                .build();
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), operation);
        dbSetup.launch();

        GuildEntity guild = dao.getAll(GuildEntity.class).get(0);
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "tag");
        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getContent(),
                "List of tags for server **"+ jda.getGuildById(Long.toString(guild.getServerId())).getName() + "**\nTest\n");
    }

    @Test
    public void testListManager() throws Exception {
        this.initManager();

        GuildEntity guild = dao.getAll(GuildEntity.class).get(0);
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "manager");
        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getContent(),
                "List of managers for server **"+ jda.getGuildById(Long.toString(guild.getServerId())).getName() + "**\nGyoo_Test\n");
    }

    @Test
    public void testListTeam() throws Exception {
        Operation operation = insertInto("team")
                .row()
                .column("ServerID", 131483070464393216L)
                .column("Name", "Test")
                .column("PlatformID", 1)
                .end()
                .build();
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), operation);
        dbSetup.launch();

        GuildEntity guild = dao.getAll(GuildEntity.class).get(0);
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "team");
        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getContent(),
                "List of teams for server **"+ jda.getGuildById(Long.toString(guild.getServerId())).getName() + "**\nTest\n");
    }

    @Test
    public void testListPermission() throws Exception {
        Operation operation = insertInto("permission")
                .row()
                .column("ServerID", 131483070464393216L)
                .column("RoleID", 0)
                .column("CommandID", 1)
                .column("Level", 0)
                .end()
                .build();
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), operation);
        dbSetup.launch();

        GuildEntity guild = dao.getAll(GuildEntity.class).get(0);
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "permissions");
        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getContent(),
                "List of permissions for server **"+ jda.getGuildById(Long.toString(guild.getServerId())).getName() + "**\nEveryone: Add\n");
    }

    @Test
    public void testUnknownOption() throws Exception {
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "");
        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getContent(),
                "Unknown option : ");
    }

}