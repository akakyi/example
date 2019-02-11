package tsp.rest.resource;

import tsp.rest.json.request.task.ShareTaskJson;
import tsp.rest.json.request.task.TaskCreateJson;
import tsp.rest.json.request.task.TaskUpdateJson;
import tsp.rest.json.response.TaskResponse;
import tsp.rest.json.response.UserResponse;
import tsp.services.RoleService;
import tsp.services.TaskService;
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

@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class TaskResource {

    @EJB
    private TaskService taskService;

    @EJB
    private AuthService authService;

    @EJB
    private UserService userService;

    @EJB
    private RoleService roleService;

    /**
     * Возвращает список всех задч.
     * @return Список задач.
     */
    @GET
    public List<TaskResponse> getAll() {
        return this.taskService.getAllTasks();
    }

    /**
     * Возвращает задачу по её id  в базе данных и валидирует результат.
     * @param id id задачи.
     * @return Задача.
     */
    @Path("/{id}")
    @GET
    public Response getById(@PathParam("id") int id, @Context HttpServletRequest req) {
        TaskResponse result = this.taskService.getTaskById(id);
        if (result != null) {
            UserResponse user = this.getRequestedUser(req);
            if ("Admin".equals(this.roleService.getById(user.getRole()).getName()) ||
                    result.getModifiedBy().equals(user.getId())) {
                return Response.ok(result, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Task not found!").build();
        }
    }

    @Path("/user")
    @GET
    public List<TaskResponse> getByUserId(@Context HttpServletRequest req) {
        UserResponse user = this.getRequestedUser(req);
        List<TaskResponse> result = this.taskService.getTasksByUserId(user.getId());
        return result;
    }

    /**
     * Возвращает список всех задач, привязанных к категории с id.
     * @param id id категории.
     * @return Список задач.
     */
    @Path("/category/{id}")
    @GET
    public List<TaskResponse> getByCategoryId(@PathParam("id") int id, @Context HttpServletRequest req) {
        UserResponse user = this.getRequestedUser(req);
        if ("Admin".equals(this.roleService.getById(user.getRole()).getName())) {
            return this.taskService.getTasksByCategoryId(id);
        } else {
            return this.taskService.getTasksByCategoryAndUserId(user.getId(), id);
        }
    }

    /**
     * Сохраняет в базе новый экземпляр задачи, валидируя пришедшие данные.
     * @param task
     * @return
     */
    @Path("/save")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(TaskCreateJson task, @Context HttpServletRequest req) {
        if (task.getName() == null || task.getModifiedBy() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Required fields are not filled").build();
        }
        UserResponse user = this.getRequestedUser(req);
        if (!user.getId().equals(task.getModifiedBy())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        this.taskService.saveTask(task);
        return Response.ok().build();
    }

    @Path("/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(TaskUpdateJson task, @Context HttpServletRequest req) {
        UserResponse user = this.getRequestedUser(req);

        boolean isExist = this.taskService.getTaskById(task.getId()) != null;
        boolean isFilled = task.getName() != null && task.getModifiedBy() != null;
        boolean isNotForbidden = user.getId().equals(task.getModifiedBy());
        if (isExist && isFilled && isNotForbidden) {
            this.taskService.updateTask(task);
            return Response.ok().build();
        } else {
            if (!isExist) {
                return Response.status(Response.Status.NOT_FOUND).entity("Task not found!").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Required fields are not filled").build();
            }
        }
    }

    @Path("/delete/{id}")
    @DELETE //Glassfish......... Покойся с мироом, Delete
//    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id, @Context HttpServletRequest req) {
        UserResponse user = this.getRequestedUser(req);
        TaskResponse task = this.taskService.getTaskById(id);
        if (task != null && user.getId().equals(task.getModifiedBy())) {
            this.taskService.deleteTaskById(id);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Task not found!").build();
        }
    }

    @Path("/share")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response share(ShareTaskJson shareTaskJson, @Context HttpServletRequest req) {
        UserResponse user = this.getRequestedUser(req);
        UserResponse recipient = this.userService.getByLogin(shareTaskJson.getSharedTo());
        TaskResponse task = this.taskService.getTaskById(shareTaskJson.getTaskId());
        if (recipient == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Recipient not found!").build();
        }
        if (task == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Task not found!").build();
        }
        if (!user.getId().equals(task.getModifiedBy())) {
            return Response.status(Response.Status.FORBIDDEN).entity("This is not your task!").build();
        }

        this.taskService.shareTask(recipient, user, task);
        return Response.ok().build();
    }

    private UserResponse getRequestedUser(HttpServletRequest req) {
        String token = req.getHeader("authorization");
        UserResponse user = this.userService.getByLogin(this.authService.getNameByToken(token));

        return user;
    }

}
