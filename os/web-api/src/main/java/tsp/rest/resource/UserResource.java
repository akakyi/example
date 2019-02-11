package tsp.rest.resource;

import tsp.rest.json.request.user.UserCreateJson;
import tsp.rest.json.request.user.UserUpdatePasJson;
import tsp.rest.json.response.UserResponse;
import tsp.services.RoleService;
import tsp.services.UserService;
import tsp.services.auth.AuthService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @EJB
    private UserService userService;

    @EJB
    private AuthService authService;

    @EJB
    private RoleService roleService;

    @GET
    public List<UserResponse> getAll() {
        return userService.getAll();
    }

    @Path("/{id}")
    @GET
    public Response getById(@PathParam("id") int id, @Context HttpServletRequest req) {
        String token = req.getHeader("authorization");
        UserResponse user = userService.getByLogin(authService.getNameByToken(token));
        UserResponse result = userService.getById(id);
        if (result != null) {
            if ("Admin".equals(roleService.getById(user.getRole()).getName()) ||
                    user.getId().equals(result.getId())) {
                return Response.ok(result, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();
        }
    }

    @Path("/login/{login}")
    @GET
    public Response getByLogin(@PathParam("login") String login, @Context HttpServletRequest req) {
        String token = req.getHeader("authorization");
        UserResponse user = userService.getByLogin(authService.getNameByToken(token));
        UserResponse result = userService.getByLogin(login);
        if (result != null) {
            if ("Admin".equals(roleService.getById(user.getRole()).getName()) ||
                    user.getLogin().equals(result.getLogin())) {
                return Response.ok(result, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();
        }
    }

    @Path("/login")
    @GET
    public Response getByLogin(@Context HttpServletRequest req) {
        String token = req.getHeader("authorization");
        UserResponse user = userService.getByLogin(authService.getNameByToken(token));
        UserResponse result = userService.getByLogin(user.getLogin());
        if (result != null) {
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/save")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(UserCreateJson user) {
        if (user.getLogin() == null || user.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Required fields are not filled").build();
        }
        if (userService.getByLogin(user.getLogin()) != null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Пользователь уже существует").build();
        }
        userService.save(user);
        return Response.ok().build();
    }

    @Path("/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    //TODO Попробовать заменить UserUpdateJson на UserResponse
    public Response update(UserResponse user, @Context HttpServletRequest req) {
        String token = req.getHeader("authorization");
        UserResponse userRes = userService.getByLogin(authService.getNameByToken(token));
        boolean isExist = userService.getById(user.getId()) != null;
        boolean isFilled = user.getLogin() != null;
        boolean isNotForbidden = userRes.getId().equals(user.getId()) &&
                userRes.getLogin().equals(user.getLogin());
        if (isExist && isFilled && isNotForbidden) {
            userService.update(user);
            return Response.ok().build();
        } else {
            if (!isExist) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
    }

    @Path("/updatePas")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePas(UserUpdatePasJson user, @Context HttpServletRequest req) {
        String token = req.getHeader("authorization");
        UserResponse userRes = userService.getByLogin(authService.getNameByToken(token));
        if (userService.getById(user.getId()) != null && userRes.getId().equals(user.getId())) {
            userService.updatePas(user);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();
        }
    }


}
