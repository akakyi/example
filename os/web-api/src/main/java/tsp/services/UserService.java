package tsp.services;

import tsp.database.dao.UserDao;
import tsp.database.entity.UserEntity;
import tsp.rest.json.request.user.UserCreateJson;
import tsp.rest.json.request.user.UserUpdatePasJson;
import tsp.rest.json.response.UserResponse;
import tsp.utils.EncryptSome;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class UserService {

    @EJB
    private UserDao userDao;

    public List<UserResponse> getAll() {
        return userDao.getAll().stream().map(UserResponse::transform).collect(Collectors.toList());
    }

    public UserResponse getById(Integer id) {
        UserEntity result = userDao.getById(id);
        return result == null ? null : UserResponse.transform(result);
    }

    public UserResponse getByLogin(String login) {
        UserEntity result = userDao.getByLogin(login);
        return result == null ? null : UserResponse.transform(result);
    }

    public void save(UserCreateJson request) {
        userDao.save(UserEntity.transform(request));
    }

    //    public void update(UserUpdateJson request) {
    public void update(UserResponse request) {
        UserEntity entity = UserEntity.transform(request);
        entity.setPassword(userDao.getById(entity.getId()).getPassword());
        userDao.update(entity);
    }

    public void updatePas(UserUpdatePasJson request) {
        UserEntity entity = userDao.getById(request.getId());
        entity.setPassword(EncryptSome.encryptMd5(request.getPassword()));
        userDao.update(entity);
    }

}
