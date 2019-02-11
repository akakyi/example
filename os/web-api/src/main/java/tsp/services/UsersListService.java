package tsp.services;

import tsp.rest.json.response.UserResponse;
import tsp.utils.EncryptSome;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.io.Serializable;
import java.util.HashMap;

//statefull дёргается разный для разных урлов. Singleton всегда один.
@Singleton
public class UsersListService implements Serializable {

    @EJB
    private UserService userService;

    private HashMap<String, String> authUsers;

    public UsersListService() {
        authUsers = new HashMap<>();
    }

    public boolean isAuth(String id) {
        return authUsers.containsKey(id);
    }

    public boolean auth(String id, String userName, String pas) {
        UserResponse userResponse = userService.getByLogin(userName);
        if (userResponse != null &&
                userResponse.getPassword().equals(EncryptSome.encryptMd5(pas))) {
            authUsers.put(id, userName);
            return true;
        } else {
            return false;
        }
    }

    public void exit(String id) {
        authUsers.remove(id);
    }

}
