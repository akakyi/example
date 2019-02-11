package tsp.rest.json.response;

import com.sun.istack.NotNull;
import tsp.database.entity.TaskEntity;
import lombok.Data;
import tsp.rest.json.BaseTask;

import java.util.Date;

@Data
public class TaskResponse extends BaseTask {

    private Integer id;
    private Date createdAt;
    private Date modifiedAt;


    public static TaskResponse transform(@NotNull TaskEntity entity) {
        TaskResponse response = new TaskResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setCategory(entity.getCategory());
        response.setDoBefore(entity.getDoBefore());
        response.setCreatedAt(entity.getCreatedAt());
        response.setModifiedAt(entity.getModifiedAt());
        response.setModifiedBy(entity.getModifiedBy());
        response.setPlase(entity.getPlase());
        response.setDid(entity.getDid());
        response.setSharedBy(entity.getSharedBy());

        return response;
    }
}
