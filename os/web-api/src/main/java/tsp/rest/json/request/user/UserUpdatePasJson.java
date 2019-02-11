package tsp.rest.json.request.user;

import lombok.Data;

@Data
public class UserUpdatePasJson {
    private Integer id;
    private String password;
}
