package tsp.utils;

import tsp.rest.json.response.TaskResponse;
import tsp.rest.json.response.UserResponse;
import tsp.services.TaskService;
import tsp.services.UserService;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Singleton
@Startup
public class ScheduledNotificationsComponent {

    @EJB
    private TaskService taskService;

    @EJB
    private UserService userService;

    @EJB
    private EMailComponent eMailComponent;

    @Schedule(hour="*", minute="*/5", second="0", persistent=false)
    public void tasksScheduleChecker() {
        final long millis = TimeUnit.MINUTES.toMillis(5);
        Date cur = new Date();
        Date nextDate = new Date(cur.getTime() + millis);

        final List<TaskResponse> tasksInPeriod
            = this.taskService.getTasksInPeriod(cur, nextDate);

        Map<String, List<TaskResponse>> tasksToUsersMap = new HashMap<>();
        if (!tasksInPeriod.isEmpty()) {
            for (TaskResponse task : tasksInPeriod) {
                final UserResponse user = this.userService.getById(task.getModifiedBy());
                final List<TaskResponse> tasks = tasksToUsersMap.get(user.getLogin());
                if (tasks == null) {
                    List<TaskResponse> userTasks = new LinkedList<>();
                    userTasks.add(task);
                    tasksToUsersMap.put(user.getLogin(), userTasks);
                } else {
                    tasks.add(task);
                }
            }
        }

        tasksToUsersMap
            .forEach(
                (k, v) -> this.eMailComponent.sendImmediateTasks(v, k)
            );
    }

}
