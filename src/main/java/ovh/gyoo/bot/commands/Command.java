package ovh.gyoo.bot.commands;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public interface Command {

    void execute(MessageReceivedEvent e, String content);
    String getDescription();
    boolean isAllowed(String serverID, String authorID);

}
