package tsp.rest.json.request.category;

import lombok.Data;

@Data
public class CategoryUpdateJson {
    private Integer id;
    private String name;
    private String description;
    private Integer createdBy;
}


