package common.api.twitch.service.entity.response;

import common.api.twitch.service.entity.Stream;

import java.util.List;

/**
 * Created by Gyoo on 01/07/2016.
 */
public class StreamsResponse {

    private List<Stream> streams;

    public List<Stream> getStreams() {
        return streams;
    }

    public void setStreams(List<Stream> streams) {
        this.streams = streams;
    }
}
