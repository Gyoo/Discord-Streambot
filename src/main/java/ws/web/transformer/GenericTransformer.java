package ws.web.transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gyoo on 26/06/2016.
 */
public abstract class GenericTransformer<E,DTO> {

    public abstract DTO modelToDto(E model);

    public List<DTO> modelToDto(List<E> model){
        List<DTO> dtos = null;
        if(model != null){
            dtos = new ArrayList<>();
            for(E e : model){
                DTO dto = modelToDto(e);
                if(dto != null){
                    dtos.add(dto);
                }
            }
        }
        return dtos;
    }

    public abstract E dtoToModel(DTO dto);

    public List<E> dtoToModel(List<DTO> dtos){
        List<E> models = null;
        if(dtos != null){
            models = new ArrayList<>();
            for(DTO dto : dtos){
                E e = dtoToModel(dto);
                if(e != null){
                    models.add(e);
                }
            }
        }
        return models;
    }

}
