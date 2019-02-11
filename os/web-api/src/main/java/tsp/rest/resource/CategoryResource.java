package tsp.rest.resource;

import tsp.rest.json.request.category.CategoryCreateJson;
import tsp.rest.json.request.category.CategoryUpdateJson;
import tsp.rest.json.response.CategoryResponse;
import tsp.rest.json.response.UserResponse;
import tsp.services.CategoryService;
import tsp.services.RoleService;
import tsp.services.UserService;
import tsp.services.auth.AuthService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @EJB
    private CategoryService categoryService;

    @EJB
    private UserService userService;

    @EJB
    private AuthService authService;

    @EJB
    private RoleService roleService;

//    @Inject
//    private SessionScopeAuthService sessionScopeAuthService;

    @GET
    public Response getAll() {
//        if (sessionScopeAuthService.isAuth()) {
            return Response.ok(categoryService.getAllCategory(), MediaType.APPLICATION_JSON).build();
//        } else {
//            return Response.status(403).build();
//        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id, @Context HttpServletRequest req) {
        String token = req.getHeader("authorization");
        UserResponse user = userService.getByLogin(authService.getNameByToken(token));
        CategoryResponse result = categoryService.getCategoryById(id);
        if (result != null) {
            if ("Admin".equals(roleService.getById(user.getRole()).getName()) ||
                    result.getCreatedBy().equals(user.getId())) {
                return Response.ok(result, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Category not found!").build();
        }
    }

    @GET
    @Path("/user")
    public List<CategoryResponse> getByUserId(@Context HttpServletRequest req) {
        String token = req.getHeader("authorization");
        UserResponse user = userService.getByLogin(authService.getNameByToken(token));
        List<CategoryResponse> result = categoryService.getCategoryByUserId(user.getId());
//        return Response.ok(result, MediaType.APPLICATION_JSON).build();
        return result;
    }

    @PUT
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(CategoryCreateJson category, @Context HttpServletRequest req) {

        if (category.getName() == null || category.getCreatedBy() == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Required fields are not filled").build();
        }
        String token = req.getHeader("authorization");
        UserResponse user = userService.getByLogin(authService.getNameByToken(token));
        if (!user.getId().equals(category.getCreatedBy())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        categoryService.saveCategory(category);
        return Response.ok().build();
    }

    @Path("/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(CategoryUpdateJson category, @Context HttpServletRequest req) {
        String token = req.getHeader("authorization");
        UserResponse user = userService.getByLogin(authService.getNameByToken(token));

        boolean isExist = categoryService.getCategoryById(category.getId()) != null;
        boolean isFilled = category.getName() != null || category.getCreatedBy() != null;
        boolean isNotForbidden = user.getId().equals(category.getCreatedBy());
        if (isExist && isFilled && isNotForbidden) {
            categoryService.updateCategory(category);
            return Response.ok().build();
        } else {
            if (!isExist) {
                return Response.status(Response.Status.NOT_FOUND).entity("Category not found!").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Required fields are not filled").build();
            }
        }
    }

    @Path("/delete/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id, @Context HttpServletRequest req) {
        String token = req.getHeader("authorization");
        UserResponse user = userService.getByLogin(authService.getNameByToken(token));
        CategoryResponse category = categoryService.getCategoryById(id);

        if (category != null &&
                user.getId().equals(category.getCreatedBy())) {
            categoryService.deleteById(id);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}
