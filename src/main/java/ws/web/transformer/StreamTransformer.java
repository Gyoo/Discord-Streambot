package ws.web.transformer;

import common.api.twitch.Twitch;
import common.api.twitch.service.entity.Stream;
import entity.StreamEntity;
import ws.web.dto.StreamDTO;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class StreamTransformer extends GenericTransformer<StreamEntity, StreamDTO> {

    private Twitch twitch;

    public StreamTransformer() {
        this.twitch = new Twitch("bl2dmxhgd1ots1xlkonz9k0e3mdeboh", "1fih3u5ve67813tj4vebev4u51m8ln");
    }

    @Override
    public StreamDTO modelToDto(StreamEntity model) {
        StreamDTO dto = null;
        if(model != null){
            dto = new StreamDTO();
            dto.setChannelName(model.getChannelName());
            dto.setGameName(model.getGameName());
            dto.setStreamTitle(model.getStreamTitle());
            dto.setPlatformID(model.getPlatform().getPlatformId());
            final Stream stream = twitch.getStream(model.getChannelName());
            dto.setPreviewURL(stream.getPreview().getMedium());
        }
        return dto;
    }

    @Override
    public StreamEntity dtoToModel(StreamDTO streamDTO) {
        return null;
    }
}
