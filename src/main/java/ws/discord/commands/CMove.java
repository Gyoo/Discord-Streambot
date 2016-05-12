package ws.discord.commands;


import dao.Dao;
import entity.GuildEntity;
import entity.local.MessageItem;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import ws.discord.messages.MessageHandler;

public class CMove extends Command{

    public static String name = "move";

    public CMove(JDA jda, Dao dao) {
        super(jda, dao);
        description = "`move <channel>` : Moves the bot announces to another channel (Must use the #channel identifier !)";
        allows.add(Allowances.MANAGERS);
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message = null;
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId(), allows, 0))
            message = new MessageBuilder().appendString("You are not allowed to use this command").build();
        else{
            GuildEntity guildEntity = dao.getLongId(GuildEntity.class, e.getGuild().getId());
            for(TextChannel channel : jda.getTextChannelsByName(content.substring(1))){
                if (channel.getGuild().getId().equals(e.getGuild().getId())){
                    guildEntity.setChannelId(Long.parseLong(channel.getId()));
                    dao.saveOrUpdate(guildEntity);
                    message = new MessageBuilder()
                            .appendString("Announces will now be done in #" + channel.getName() + " !")
                            .build();
                }
            }
        }
        assert null != message;
        MessageHandler.getInstance().addToQueue(e.getTextChannel().getId(), MessageItem.Type.GUILD, message);
    }
}
