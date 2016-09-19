package common.api.twitch.service.entity;

/**
 * Created by Gyoo on 29/06/2016.
 */
public class Stream {

    private String game;
    private Channel channel;
    private Preview preview;

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Preview getPreview() {
        return preview;
    }

    public void setPreview(Preview preview) {
        this.preview = preview;
    }
}
