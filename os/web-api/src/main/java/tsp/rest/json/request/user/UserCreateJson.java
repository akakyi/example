package tsp.rest.json.request.user;

import lombok.Data;

@Data
public class UserCreateJson {

    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Integer role;
}
