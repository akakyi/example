package tsp.services;

import tsp.rest.json.response.RoleResponse;
import tsp.rest.json.response.UserResponse;
import tsp.utils.EncryptSome;

import javax.ejb.EJB;
import java.io.Serializable;

//@SessionScoped
public class SessionScopeAuthService implements Serializable {

    @EJB
    private UserService userService;

    @EJB
    private RoleService roleService;

    private String userName;

    private Integer id;

    private boolean auth = false;

    private String role;

    public boolean auth(String userName, String password) {
        UserResponse userResponse = userService.getByLogin(userName);
        if (userResponse != null &&
                userResponse.getPassword().equals(EncryptSome.encryptMd5(password))) {
            this.auth = true;
            this.userName = userName;
            this.id = userResponse.getId();
            if (userResponse.getRole() != null) {
                RoleResponse role = roleService.getById(userResponse.getRole());
                if (role != null) {
                    this.role = role.getName();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public Integer getId() {
        return id;
    }

    public boolean isAuth() {
        return auth;
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return role;
    }
}
