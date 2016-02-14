package ovh.gyoo.bot.data;

public class StreamInfo {

    String link = "http://twitch.tv/";
    String name;
    String title;
    String game;

    public StreamInfo(String name, String game, String title) {
        this.link += name;
        this.name = name;
        this.game = game;
        this.title = title;
    }

    public StreamInfo(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getGame() {
        return game;
    }

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof StreamInfo){
            StreamInfo ptr = (StreamInfo) v;
            retVal = ptr.getName().equals(this.name);
        }

        return retVal;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
