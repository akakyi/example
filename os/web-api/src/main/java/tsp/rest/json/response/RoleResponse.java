package tsp.rest.json.response;

import tsp.database.entity.RoleEntity;
import lombok.Data;

@Data
public class RoleResponse {

    private Integer id;
    private String name;

    public static RoleResponse transform(RoleEntity entity) {
        RoleResponse response = new RoleResponse();
        response.id = entity.getId();
        response.name = entity.getName();
        return response;
    }
}
