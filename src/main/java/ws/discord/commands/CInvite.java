package ws.discord.commands;

import dao.Dao;
import entity.local.MessageItem;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;

public class CInvite extends Command{

    public static String name = "invite";

    public CInvite(JDA jda, Dao dao) {
        super(jda, dao);
        description = "`invite` : Gives an invite link so people can get the bot on their own server !";
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message = new MessageBuilder()
                .appendString("https://discordapp.com/oauth2/authorize?&client_id=170832003715956746&scope=bot&permissions=150528")
                .build();
        MessageHandler.getInstance().addToQueue(e.getTextChannel().getId(), MessageItem.Type.GUILD, message);
    }
}
