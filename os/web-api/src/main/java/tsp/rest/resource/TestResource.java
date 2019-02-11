package tsp.rest.resource;

import tsp.database.dao.UserDao;
import tsp.database.entity.UserEntity;
import tsp.services.UsersListService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import java.util.List;

@Path("/test")
@Stateless
public class TestResource {

    @EJB
    private UserDao userDao;

    @EJB
    private UsersListService usersListService;

//    @Inject
//    private SessionScopeAuthService sessionScopeAuthService;
//
//    @GET
//    public List<UserEntity> getSome(@Context HttpServletRequest request) {
////        UserEntity entity = new UserEntity();
////        entity.setFirstName("aaa");
////        entity.setLogin("daoTestMd5Apache");
////        entity.setDescription("hui");
////        entity.setRole(0);
////        userDao.save(entity);
//        if (!usersListService.isAuth(request.getSession().getId())) {
//            usersListService.auth(request.getSession().getId(), "admin", "admin");
//            return null;
//        }
//        UserEntity entity = userDao.getById(4);
////        userDao.delete(entity);
//
//        return null;
//    }

    @GET
    @Path("/{id}")
    public void doSome(@PathParam("id") int id, @Context HttpServletRequest request) {
        if (!usersListService.isAuth(request.getSession().getId())) {
            usersListService.auth(request.getSession().getId(), "testAuth", "test");
            return;
        }
        UserEntity entity = userDao.getById(id);
//        userDao.delete(entity);
    }

    @GET
    @Path("/sessionScope")
    public void sscope(@Context HttpServletRequest request) {
//        if (!sessionScopeAuthService.isAuth()) {
//            sessionScopeAuthService.auth("admin", "admin");
//            return;
//        }
//
//        sessionScopeAuthService.getUserName();
    }

}
