package tsp.rest.resource;

import tsp.rest.json.response.RoleResponse;
import tsp.services.RoleService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/role")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class RoleResource {

    @EJB
//    @Inject
    private RoleService roleService;

//    @Inject
//    private SessionScopeAuthService sessionScopeAuthService;

    @GET
    public List<RoleResponse> get() {
//        roleDao.list().stream();
//        if (!sessionScopeAuthService.isAuth() || sessionScopeAuthService.getRole() == null
//                || !sessionScopeAuthService.getRole().equals("Admin")) {
//            return null;
//        }
        return roleService.getAll();
    }

    /**
     * just for fun.
     * @param id role id.
     * @return role.
     */
    @Path("/{id}")
    @GET
    public Response getById(@PathParam("id") int id) {
//        if (!sessionScopeAuthService.isAuth() || sessionScopeAuthService.getRole() == null
//                || !sessionScopeAuthService.getRole().equals("Admin")) {
//            return Response.status(403).build();
//        }
        RoleResponse result = roleService.getById(id);
        if (result != null) {
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Role not found!").build();
        }
    }
}
