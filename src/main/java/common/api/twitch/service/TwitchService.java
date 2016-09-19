package common.api.twitch.service;

import common.api.twitch.service.entity.response.StreamsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import common.api.twitch.service.entity.response.StreamResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by Gyoo on 29/06/2016.
 */
public interface TwitchService {

    @GET("streams/{stream}")
    Call<StreamResponse> getStream(@Path("stream") String stream);

    @GET("streams")
    Call<StreamsResponse> getStreamsWithParams(@QueryMap Map<String, String> filters);

}
