package common.api.twitch;

import common.api.twitch.service.HeaderInterceptor;
import common.api.twitch.service.TwitchService;
import common.api.twitch.service.entity.Stream;
import common.api.twitch.service.entity.response.StreamsResponse;
import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import common.api.twitch.service.entity.response.StreamResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Twitch {

    private TwitchService service;

    public Twitch(String clientID, String accessToken) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new HeaderInterceptor(accessToken, clientID)).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twitch.tv/kraken/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        this.service = retrofit.create(TwitchService.class);
    }

    public Stream getStream(String stream){
        try {
            return service.getStream(stream).execute().body().getStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getStreamAsync(String stream, Callback<StreamResponse> callback){
        service.getStream(stream).enqueue(callback);
    }

    public List<Stream> getStreamsWithParams(Map<String, String> params){
        try {
            return service.getStreamsWithParams(params).execute().body().getStreams();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getStreamsWithParamsAsync(Map<String, String> params, Callback<StreamsResponse> callback){
        service.getStreamsWithParams(params).enqueue(callback);
    }
}
