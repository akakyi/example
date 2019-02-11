package tsp.rest.resource;

import tsp.database.entity.AuthTokenEntity;
import tsp.rest.json.request.user.AuthorizationJson;
import tsp.rest.json.response.UserResponse;
import tsp.services.UserService;
import tsp.services.auth.AuthService;
import tsp.utils.EncryptSome;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@Path("/auth")
public class AuthResource {

//    @Inject
//    private SessionScopeAuthService sessionScopeAuthService;

    @EJB
    private AuthService authService;

    @EJB
    private UserService userService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response auth(AuthorizationJson request) {
        UserResponse userResponse = userService.getByLogin(request.getLogin());
        if (userResponse != null &&
                userResponse.getPassword().equals(EncryptSome.encryptMd5(request.getPassword()))) {
            List<AuthTokenEntity> authTokenEntity = authService.findByUserName(userResponse.getLogin());
            if (authTokenEntity.isEmpty()) {
                return Response.ok().header("authorization", authService.addToken(request.getLogin())).build();
            } else {
                return Response.ok().header("authorization", authTokenEntity.iterator().next().getToken()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Net!").build();
        }
    }

    @GET
    @Path("/exit")
    public Response exit(@Context HttpServletRequest req) {
        if (authService.getNameByToken(req.getHeader("authorization")) != null) {
            authService.deleteByToken(req.getHeader("authorization"));
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/check")
    public Response check() {
        return Response.ok().build();
    }

}
