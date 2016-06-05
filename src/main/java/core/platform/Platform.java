package core.platform;

public interface Platform {

    int getId();
    void checkStreams(Long guildID);
    void checkStillOnline();
}
