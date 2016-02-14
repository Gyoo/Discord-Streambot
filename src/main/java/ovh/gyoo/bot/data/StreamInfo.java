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

        else if (v instanceof String){
            String ptr = (String) v;
            retVal = this.name.equals(ptr);
        }

        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

}
