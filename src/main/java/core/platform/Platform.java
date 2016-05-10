package core.platform;

import entity.GuildEntity;

public interface Platform {

    int getId();
    void checkStreams(Long guildID);
    void checkStillOnline();
}
