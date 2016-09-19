package ws.web.transformer;

import entity.GameEntity;
import ws.web.dto.GameDTO;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class GameTransformer extends GenericTransformer<GameEntity, GameDTO> {

    @Override
    public GameDTO modelToDto(GameEntity model) {
        GameDTO dto = null;
            if(model != null){
                dto = new GameDTO();
                dto.setName(model.getName());
                dto.setPlatformID(model.getPlatform().getPlatformId());
            }
        return dto;
    }

    @Override
    public GameEntity dtoToModel(GameDTO gameDTO) {
        return null;
    }
}
