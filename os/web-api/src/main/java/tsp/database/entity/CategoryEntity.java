package tsp.database.entity;

import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import tsp.rest.json.request.category.CategoryCreateJson;
import tsp.rest.json.request.category.CategoryUpdateJson;

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
@Table(name = "category")
@Setter
public class CategoryEntity {
    private Integer id;
    private String name;
    private String description;
    private Date createdAt;
    private Date modifietAt;
    private Integer createdBy;

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

    @Generated(value = GenerationTime.NEVER)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    @Generated(value = GenerationTime.ALWAYS)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    public Date getModifietAt() {
        return modifietAt;
    }

    @Column(name = "modified_by")
    public Integer getCreatedBy() {
        return createdBy;
    }

    public static CategoryEntity transform(CategoryCreateJson category) {
        CategoryEntity result = new CategoryEntity();
        result.setName(category.getName());
        result.setDescription(category.getDescription());
        result.setCreatedBy(category.getCreatedBy());
        return result;
    }

    public static CategoryEntity transform(CategoryUpdateJson category) {
        CategoryEntity result = new CategoryEntity();
        result.setId(category.getId());
        result.setName(category.getName());
        result.setDescription(category.getDescription());
        result.setCreatedBy(category.getCreatedBy());
        return result;
    }
}

