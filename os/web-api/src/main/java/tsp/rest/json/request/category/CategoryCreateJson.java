package tsp.rest.json.request.category;

import lombok.Data;

@Data
public class CategoryCreateJson {
    private String name;
    private String description;
    private Integer createdBy;
}
