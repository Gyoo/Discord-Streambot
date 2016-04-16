package ovh.gyoo.bot.commands;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.utils.InviteUtil;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.MessageItem;

public class CInvite implements Command{

    public static String name = "invite";
    private static String description = "`invite` : Gives an invite link so people can get the bot on their own server !";

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        MessageItem message = new MessageItem(e.getTextChannel().getId(), MessageItem.Type.GUILD, new MessageBuilder()
                .appendString("https://discordapp.com/oauth2/authorize?&client_id=170832003715956746&scope=bot&permissions=150528")
                .build());
        DiscordInstance.getInstance().addToQueue(message);
    }

    @Override
    public String getDescription(){
        return description;
    }

    @Override
    public boolean isAllowed(String serverID, String authorID) {
        return true;
    }

}
