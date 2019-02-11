package tsp.rest.json.request.user;

import lombok.Data;

@Data
public class AuthorizationJson {

    private String login;

    private String password;
    
}
