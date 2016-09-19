package ws.web.transformer;

import entity.TeamEntity;
import ws.web.dto.TeamDTO;

/**
 * Created by Gyoo on 26/06/2016.
 */
public class TeamTransformer extends GenericTransformer<TeamEntity, TeamDTO>{
    @Override
    public TeamDTO modelToDto(TeamEntity model) {
        TeamDTO dto = null;
        if(model != null){
            dto = new TeamDTO();
            dto.setName(model.getName());
            dto.setPlatformID(model.getPlatform().getPlatformId());
        }
        return dto;
    }

    @Override
    public TeamEntity dtoToModel(TeamDTO teamDTO) {
        return null;
    }
}
