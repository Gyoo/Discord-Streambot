package ovh.gyoo.bot.handlers;

import com.mb3364.http.RequestParams;
import com.mb3364.twitch.api.handlers.StreamResponseHandler;
import com.mb3364.twitch.api.handlers.StreamsResponseHandler;
import com.mb3364.twitch.api.handlers.TeamResponseHandler;
import com.mb3364.twitch.api.models.Stream;
import com.mb3364.twitch.api.models.Team;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TeamUtils {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
/*
    public static List<Stream> GetChannels(Team team) {
        return GetChannels(team, false);
    }

*/
    public static void GetChannels(Team team, final TeamInfoRequestHandler handler) {
        TeamChannelsRequest request = new TeamChannelsRequest(team, handler);
        request.run();
    }

    /*
    public static List<Stream> GetChannels(Team team, boolean deep) {
        if(team == null) {
            return new ArrayList<>();
        }
        final List<StreamsResponseWaitHandler> streamObj = new ArrayList<>();

        // Use Twitch API v2 for finding channels belonging to team
        String memberListBase = "https://api.twitch.tv/api/team/" + team.getName() + "/all_channels.json";
        boolean readingMembers = true;
        int numMembers = 0;

        // Load the team channels
        JSONObject document = null;
        try {
            document = readJsonFromUrl(memberListBase);
        } catch (IOException e) {
            return new ArrayList<Stream>();
        }
        JSONArray channels = document.getJSONArray("channels");

        while(readingMembers) {
            if(numMembers > channels.length()){
                readingMembers = false;
                break;
            }

            JSONObject channel = channels.getJSONObject(numMembers).getJSONObject("channel");
            Object lockObj = new Object();
            if(channel.getString("status").equals("live") || deep) {
                numMembers++;
                // Get a valid stream object from the channel's name
                StreamsResponseWaitHandler handler = new StreamsResponseWaitHandler();
                TwitchChecker.getTwitch().streams().get(channel.getString("name"), handler);
                streamObj.add(handler);
            } else {
                readingMembers = false;
            }
        }

        List<Stream> users = new ArrayList<Stream>();

        // Spinlock to wait for  users to be found (or not found)
        for(StreamsResponseWaitHandler handler : streamObj) {
            while(!handler.found) {
                Thread.yield();
                //Thread.sleep(100);
            }
            if(handler.stream != null) users.add(handler.stream);
        }

        return users;
    }

    */

    // Wrapper for the async twitch api
    public static void getTeam(String name, TeamRequestHandler handler) {
        TwitchChecker.getTwitch().teams().get(name, new TeamResponseHandler() {
            @Override
            public void onSuccess(Team team) {
                handler.onSuccess(team);
            }

            @Override
            public void onFailure(int i, String s, String s1) {
                handler.onFailure(i, s, s1);
            }

            @Override
            public void onFailure(Throwable throwable) {
                handler.onFailure(throwable);
            }
        });
    }

    private static class TeamChannelsRequest implements Runnable {

        private Team team;
        private TeamInfoRequestHandler handler;

        public TeamChannelsRequest(Team team, TeamInfoRequestHandler handler) {
            this.team = team;
            this.handler = handler;
        }

        @Override
        public void run() {
            if(team == null) {
                handler.onFailure(-1, "No Team", "No Team");
                return;
            }
            final List<StreamsResponseWaitHandler> streamObj = new ArrayList<>();

            // Use Twitch API v2 for finding channels belonging to team
            String memberListBase = "https://api.twitch.tv/api/team/" + team.getName() + "/all_channels.json";
            boolean readingMembers = true;
            int numMembers = 0;

            // Load the team channels
            JSONObject document = null;
            try {
                document = readJsonFromUrl(memberListBase);
            } catch (IOException e) {
                handler.onFailure(-1, "Invalid JSON Object", "Invalid JSON Object");
            }
            JSONArray channels = document.getJSONArray("channels");
            int numChannels = channels.length();

            for(int j=0; j<=numChannels/100; ++j) {
                String channelList = "";
                if(numChannels-j*100 <= 0) break;
                for(int i=0; i<Math.min(100, numChannels-j*100); ++i) {
                    JSONObject channel = channels.getJSONObject(i + j*100).getJSONObject("channel");
                    channelList += "," + channel.getString("name");
                }
                channelList = channelList.substring(1);
                RequestParams rp = new RequestParams();
                rp.put("channel", channelList);
                StreamsResponseWaitHandler handler = new StreamsResponseWaitHandler();
                rp.put("limit", 100);

                TwitchChecker.getTwitch().streams().get(rp, handler);

                streamObj.add(handler);
            }

            /*
            while(readingMembers) {
                if(numMembers > channels.length()){
                    readingMembers = false;
                    break;
                }

                JSONObject channel = channels.getJSONObject(numMembers).getJSONObject("channel");
                Object lockObj = new Object();

                if(channel.getString("status").equals("live")) {
                    numMembers++;
                    // Get a valid stream object from the channel's name
                    StreamsResponseWaitHandler handler = new StreamsResponseWaitHandler();
                    TwitchChecker.getTwitch().streams().get(channel.getString("name"), handler);
                    streamObj.add(handler);
                } else {
                    readingMembers = false;
                }
            }
            */

            List<Stream> users = new ArrayList<Stream>();

            // Spinlock to wait for  users to be found (or not found)
            for(StreamsResponseWaitHandler handler : streamObj) {
                while(!handler.found) {
                    Thread.yield();
                    //Thread.sleep(100);
                }
                if(handler.streams != null) {
                    for(Stream s : handler.streams) users.add(s);
                }
            }
            handler.onSuccess(users);
        }
    }

    /*
    public static Team getTeam(String name) {
        TeamResponseWaitHandler handler = new TeamResponseWaitHandler();
        TwitchChecker.getTwitch().teams().get(name, handler);

        // Spinlock until handler has finished executing
        while(!handler.found) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return handler.team;
    }

    static class TeamResponseWaitHandler implements TeamResponseHandler {

        public boolean found = false;
        public Team team = null;
        @Override
        public void onFailure(int i, String s, String s1) {
            found = true;
        }

        @Override
        public void onFailure(Throwable throwable) {
            found = true;
        }

        @Override
        public void onSuccess(Team team) {
            this.team = team;
            found = true;
        }
    }
    */


    static class StreamsResponseWaitHandler implements StreamsResponseHandler {

        public boolean found = false;
        public List<Stream> streams = null;
        @Override
        public void onFailure(int i, String s, String s1) {
            found = true;
        }

        @Override
        public void onFailure(Throwable throwable) {
            found = true;
        }

        @Override
        public void onSuccess(int i, List<Stream> list) {
            this.streams = new ArrayList<>(list);
            found = true;
        }
    }
}
