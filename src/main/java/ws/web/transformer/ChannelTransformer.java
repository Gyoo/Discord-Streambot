package ws.web.transformer;

import entity.ChannelEntity;
import ws.web.dto.ChannelDTO;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class ChannelTransformer extends GenericTransformer<ChannelEntity, ChannelDTO> {
    @Override
    public ChannelDTO modelToDto(ChannelEntity model) {
        ChannelDTO dto = null;
        if(model != null){
            dto = new ChannelDTO();
            dto.setName(model.getName());
            dto.setPlatformID(model.getPlatform().getPlatformId());
        }
        return dto;
    }

    @Override
    public ChannelEntity dtoToModel(ChannelDTO channelDTO) {
        return null;
    }
}
