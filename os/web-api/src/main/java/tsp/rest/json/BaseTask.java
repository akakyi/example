package tsp.rest.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public abstract class BaseTask {
    private String name;
    private String description;
    private Integer category;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+4")
    private Date doBefore;
    private Integer modifiedBy;
    private String plase;
    private Boolean did;
    private String sharedBy;


}
