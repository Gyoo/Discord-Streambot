package common.api.twitch.service.entity.response;

import common.api.twitch.service.entity.Stream;

/**
 * Created by Gyoo on 29/06/2016.
 */
public class StreamResponse {

    private Stream stream;

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }
}
