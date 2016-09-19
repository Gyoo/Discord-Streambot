package common.api.twitch.service;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by Gyoo on 29/06/2016.
 */
public class HeaderInterceptor implements Interceptor {

    private String accessToken;
    private String clientID;

    public HeaderInterceptor(String accessToken, String clientID) {
        this.accessToken = accessToken;
        this.clientID = clientID;
    }

    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();
        /*if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
            return chain.proceed(originalRequest);
        }*/

        Request compressedRequest = originalRequest.newBuilder()
                .header("Client-ID", clientID)
                .header("Authorization", "OAuth " + accessToken)
                .header("Accept", "application/vnd.twitchtv.v3+json")
                .build();
        return chain.proceed(compressedRequest);
    }
}