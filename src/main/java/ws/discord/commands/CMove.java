package ws.discord.commands;


import dao.Dao;
import entity.GuildEntity;
import entity.local.MessageCreateAction;
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
        description = "`move <channel>` : Moves the bot announcements to another channel (Must use the channel identifier ! Example : `#public` )";
        allows.add(Allowances.MANAGERS);
    }

    @Override
    public void execute(MessageReceivedEvent e, String content) {
        Message message = null;
        String channelId = e.getTextChannel().getId();
        if(!isAllowed(e.getGuild().getId(), e.getAuthor().getId(), allows, 0, null))
            message = new MessageBuilder().appendString("You are not allowed to use this command").build();
        else if(!content.startsWith("#")){
            message = new MessageBuilder()
                    .appendString("Error : this is not a channel name. It must start with a #. Example : `#public`")
                    .build();
        }
        else{
            GuildEntity guildEntity = dao.getLongId(GuildEntity.class, e.getGuild().getId());
            boolean found = false;
            for(TextChannel channel : jda.getTextChannelsByName(content.substring(1))){
                if (channel.getGuild().getId().equals(e.getGuild().getId())){
                    guildEntity.setChannelId(Long.parseLong(channel.getId()));
                    dao.saveOrUpdate(guildEntity);
                    message = new MessageBuilder()
                            .appendString("Announcements will now be done here !")
                            .build();
                    channelId = channel.getId();
                    found = true;
                }
            }
            if(!found){
                message = new MessageBuilder()
                        .appendString("Error : Channel not found on your server.")
                        .build();
            }
        }
        assert null != message;
        MessageHandler.getInstance().addCreateToQueue(channelId, MessageCreateAction.Type.GUILD, message);
    }
}
