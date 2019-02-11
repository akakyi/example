package tsp.rest.json.response;

import com.sun.istack.NotNull;
import tsp.database.entity.CategoryEntity;
import lombok.Data;

import java.util.Date;

@Data
public class CategoryResponse {

    private Integer id;
    private String name;
    private String description;
    private Date createdAt;
    private Date modifietAt;
    private Integer createdBy;


    public static CategoryResponse transform(@NotNull CategoryEntity entity) {
        CategoryResponse response = new CategoryResponse();
        response.id = entity.getId();
        response.name = entity.getName();
        response.createdAt = entity.getCreatedAt();
        response.modifietAt = entity.getModifietAt();
        response.createdBy = entity.getCreatedBy();
        response.description = entity.getDescription();
        return response;
    }

}

