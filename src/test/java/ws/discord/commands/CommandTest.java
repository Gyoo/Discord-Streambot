package ws.discord.commands;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import common.CommonData;
import common.CommonOperations;
import common.PropertiesReader;
import dao.Dao;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import org.testng.annotations.AfterMethod;
import ws.discord.messages.MessageHandler;

import javax.security.auth.login.LoginException;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;

public abstract class CommandTest {

    protected static JDA jda;

    static {
        try {
            String token = PropertiesReader.getInstance().getProp().getProperty("bot.token");
            jda = new JDABuilder().setBotToken(token).buildBlocking();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected Dao dao;
    protected Command command;

    protected static DbSetupTracker dbSetupTracker = new DbSetupTracker();

    protected void setUp() throws Exception {
        Operation operation = sequenceOf(
                CommonOperations.TRUNCATE_ALL,
                CommonOperations.INSERT_COMMON_DATA
        );
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination(CommonData.URL, CommonData.user, CommonData.password), operation);
        dbSetup.launch();
        dao = new Dao();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        MessageHandler.getQueue().clear();
    }

}
