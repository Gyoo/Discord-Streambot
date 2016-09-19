package ws.web.transformer;

import entity.TagEntity;
import ws.web.dto.TagDTO;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class TagTransformer extends GenericTransformer<TagEntity, TagDTO>{

    @Override
    public TagDTO modelToDto(TagEntity model) {
        TagDTO dto = null;
        if(model != null){
            dto = new TagDTO();
            dto.setName(model.getName());
        }
        return dto;
    }

    @Override
    public TagEntity dtoToModel(TagDTO tagDTO) {
        return null;
    }
}
