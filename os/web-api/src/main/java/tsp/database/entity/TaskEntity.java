package tsp.database.entity;

import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import tsp.rest.json.BaseTask;
import tsp.rest.json.request.task.TaskCreateJson;
import tsp.rest.json.request.task.TaskUpdateJson;
import tsp.rest.json.response.TaskResponse;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "tasks")
@Setter
public class TaskEntity {

    private Integer id;
    private String name;
    private String description;
    private Integer category;
    private Date doBefore;
    private Date createdAt;
    private Date modifiedAt;
    private Integer modifiedBy;
    private String plase;
    private Boolean did;
    private String sharedBy;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @Column(name = "category")
    public Integer getCategory() {
        return category;
    }

    @Column(name = "do_before")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDoBefore() {
        return doBefore;
    }

    @Column(name = "created_at")
    @Generated(value = GenerationTime.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedAt() {
        return createdAt;
    }

    @Column(name = "modified_at")
    @Generated(value = GenerationTime.ALWAYS)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getModifiedAt() {
        return modifiedAt;
    }

    @Column(name = "modified_by")
    public Integer getModifiedBy() {
        return modifiedBy;
    }

    @Column(name = "plase")
    public String getPlase() {
        return plase;
    }

    @Column(name = "isDid")
    public Boolean getDid() {
        return did;
    }

    @Column(name = "shared_by")
    public String getSharedBy() {
        return this.sharedBy;
    }


    public static TaskEntity transform(TaskCreateJson task) {
        TaskEntity result = baseTransform(task);

        return result;
    }

    public static TaskEntity transform(TaskUpdateJson task) {
        TaskEntity result = baseTransform(task);
        result.setId(task.getId());

        return result;
    }

    public static TaskEntity transform(TaskResponse task) {
        TaskEntity result = baseTransform(task);
        result.setId(task.getId());
        result.setSharedBy(task.getSharedBy());

        return result;
    }

    private static TaskEntity baseTransform(BaseTask task) {
        TaskEntity result = new TaskEntity();

        result.setName(task.getName());
        result.setCategory(task.getCategory());
        result.setDescription(task.getDescription());
        result.setDoBefore(task.getDoBefore());
        result.setModifiedBy(task.getModifiedBy());
        result.setPlase(task.getPlase());
        result.setDid(task.getDid());
        result.setSharedBy(task.getSharedBy());

        return result;
    }


}
