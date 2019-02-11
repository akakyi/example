package tsp.rest.json.request.task;

import lombok.Data;
import tsp.rest.json.BaseTask;

@Data
public class TaskUpdateJson extends BaseTask {
    private Integer id;
}
