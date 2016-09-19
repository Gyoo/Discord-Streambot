package ws.discord.commands;
import static com.ninja_squad.dbsetup.Operations.*;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import common.CommonData;
import common.CommonOperations;
import common.util.HibernateUtil;
import dao.Dao;
import entity.*;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.impl.MessageImpl;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.hibernate.Hibernate;
import org.testng.Assert;
import org.testng.annotations.*;
import ws.discord.messages.MessageHandler;

import java.util.ArrayList;
import java.util.List;

public class CAddTest extends CommandTest{

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        command = new CAdd(jda, dao);
    }

    private void initPermissionsAdd(){
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), CommonOperations.INSERT_PERMISSION_ADD);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    private void initManager(){
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), CommonOperations.INSERT_MANAGER);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void testExecuteNotAllowed() throws Exception {
        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "test");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(), "You are not allowed to use this command");
    }

    @Test
    public void testExecutePermission() throws Exception {
        Operation operation =
                insertInto("permission")
                    .row()
                        .column("ServerID", 131483070464393216L)
                        .column("RoleID", 0)
                        .column("CommandID", 1L)
                        .column("Level", 1)
                        .end()
                    .build();
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);

        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "game test");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(),
                "Your request will be treated by a manager soon! (type `!streambot list manager` to check the managers list)");
        QueueitemEntity queueitemEntity = dao.getAll(QueueitemEntity.class).get(0);
        Assert.assertEquals(queueitemEntity.getCommand(),
                "`!streambot add game test`");
    }

    @Test
    public void testExecuteMissingOption() throws Exception {
        initPermissionsAdd();

        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "");

        Assert.assertEquals(1, MessageHandler.getQueue().size());
        Assert.assertEquals(MessageHandler.getQueue().peek().getMessage().getRawContent(),
                "Missing option");
    }

    @Test
    public void testAddGame() throws Exception{
        initPermissionsAdd();

        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "game Doom | Doom");

        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().poll().getMessage().getRawContent(),
                "Game Doom added to the game list\nGame Doom is already in the game list\n");

        List<GameEntity> gameEntities = dao.getAll(GameEntity.class);
        Assert.assertEquals(1, gameEntities.size());
        Assert.assertEquals("Doom", gameEntities.get(0).getName());
    }

    @Test
    public void testAddChannel() throws Exception{
        initPermissionsAdd();

        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "channel gyoo_ | gyoo_");

        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().poll().getMessage().getRawContent(),
                "Channel gyoo_ added to the channels list\nChannel gyoo_ is already in the channels list\n");

        List<ChannelEntity> channelEntities = dao.getAll(ChannelEntity.class);
        Assert.assertEquals(1, channelEntities.size());
        Assert.assertEquals("gyoo_", channelEntities.get(0).getName());
    }

    @Test
    public void testAddTag() throws Exception{
        initPermissionsAdd();

        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "tag test | test");

        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().poll().getMessage().getRawContent(),
                "Tag test added to the tags list\nTag test is already in the tags list\n");

        List<TagEntity> tagEntities = dao.getAll(TagEntity.class);
        Assert.assertEquals(1, tagEntities.size());
        Assert.assertEquals("test", tagEntities.get(0).getName());
    }

    @Test
    public void testAddTeam() throws Exception{
        initPermissionsAdd();

        User user = jda.getUserById("63263941735755776");
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol");
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "team test | test");

        Assert.assertEquals(MessageHandler.getQueue().size(), 1);
        Assert.assertEquals(MessageHandler.getQueue().poll().getMessage().getRawContent(),
                "Team test added to the teams list\nTeam test is already in the teams list\n");

        List<TeamEntity> teamEntities = dao.getAll(TeamEntity.class);
        Assert.assertEquals(1, teamEntities.size());
        Assert.assertEquals("test", teamEntities.get(0).getName());
    }

    @Test
    public void testAddManagers() throws Exception{
        initManager();

        User user = jda.getUserById("63263941735755776");
        List<User> mentions = new ArrayList<>();
        mentions.add(jda.getUserById("63263941735755776"));
        Message message = new MessageImpl("", null).setChannelId("131483070464393216").setAuthor(user).setContent("lol").setMentionedUsers(mentions);
        MessageReceivedEvent mre = new MessageReceivedEvent(jda, 1, message);
        command.execute(mre, "manager @Gyoo");

        Assert.assertEquals(MessageHandler.getQueue().size(), 1);

        List<ManagerEntity> managerEntities = dao.getAll(ManagerEntity.class);
        Assert.assertEquals(managerEntities.size(), 2);
    }

}