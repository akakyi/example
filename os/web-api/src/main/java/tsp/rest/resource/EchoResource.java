package tsp.rest.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tsp.rest.json.response.EchoResponse;
import tsp.rest.json.response.TaskResponse;
import tsp.services.TaskService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path("/echo")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class EchoResource {
    private final static Logger logger = LoggerFactory.getLogger(EchoResource.class);

    @EJB
    private TaskService taskService;


    @GET
    @Path("/{echo}")
    public EchoResponse echo(@PathParam("echo") String msg) {
        logger.debug("echo {%s}", msg);
        EchoResponse result = new EchoResponse();
        result.setMessage(msg);

//        entityManager.find(UserEntity.class, 0);

        Date stime = null;
        Date etime = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            stime = format.parse("2018.12.27 21:43");
            etime = format.parse("2018.12.27 21:48");
        } catch (ParseException e) {

        }
//        final Calendar instance = Calendar.getInstance();
//        instance.set(2018, 12, 27, 21, 43);
//        final Date stime = instance.getTime();
//
//        instance.set(2018, 12, 27, 21, 48);
//        final Date etime = instance.getTime();

        final List<TaskResponse> tasksInPeriod = this.taskService.getTasksInPeriod(stime, etime);

        return result;
    }
}
