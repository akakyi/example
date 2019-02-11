package tsp.rest.json.request.task;

import lombok.Data;

@Data
public class ShareTaskJson {

    private Integer taskId;
    private String sharedTo;

}
