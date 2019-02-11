package tsp.services;

import tsp.database.dao.TaskDao;
import tsp.database.entity.TaskEntity;
import tsp.rest.json.request.task.TaskCreateJson;
import tsp.rest.json.request.task.TaskUpdateJson;
import tsp.rest.json.response.TaskResponse;
import tsp.rest.json.response.UserResponse;
import tsp.utils.EMailComponent;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class TaskService {

    @EJB
    private TaskDao taskDao;

    @EJB
    private EMailComponent eMailComponent;

    public List<TaskResponse> getAllTasks() {
        final List<TaskResponse> allTasks = this.taskDao.getAll().stream()
            .map(TaskResponse::transform)
            .collect(Collectors.toList());

        return allTasks;
    }

    public List<TaskResponse> getTasksInPeriod(Date startDate, Date endDate) {
        final List<TaskResponse> result = this.taskDao.getBetween(startDate, endDate).stream()
            .map(TaskResponse::transform)
            .collect(Collectors.toList());

        return result;
    }

    public TaskResponse getTaskById(int id) {
        TaskEntity result = this.taskDao.getById(id);
        if (result != null) {
            return TaskResponse.transform(this.taskDao.getById(id));
        } else {
            return null;
        }
    }

    public List<TaskResponse> getTasksByUserId(int id) {
        List<TaskResponse> result =
                this.taskDao.findByUser(id).stream()
                    .map(TaskResponse::transform)
                    .collect(Collectors.toList());
        return result;
    }

    public List<TaskResponse> getTasksByCategoryAndUserId(int userId, int categoryId) {
        List<TaskResponse> tasks = getTasksByCategoryId(categoryId);
        LinkedList<TaskResponse> result = new LinkedList<>();
        for (TaskResponse task : tasks) {
            if (task.getModifiedBy().equals(userId)) {
                result.add(task);
            }
        }
        return result;
    }

    public void saveTask(TaskCreateJson task) {
        this.taskDao.save(TaskEntity.transform(task));
    }

    public void updateTask(TaskUpdateJson task) {
        TaskEntity readyTask = TaskEntity.transform(task);
        readyTask.setCreatedAt(getTaskById(task.getId()).getCreatedAt());
        this.taskDao.update(readyTask);
    }

    public List<TaskResponse> getTasksByCategoryId(int id) {
        List<TaskResponse> result =
                this.taskDao.findByCategory(id).stream().map(TaskResponse::transform).collect(Collectors.toList());
        return result;
    }

    public void deleteTaskById(int id) {
        TaskEntity entity = this.taskDao.getById(id);
        this.taskDao.delete(entity);
    }
    
    public void shareTask(UserResponse recipient, UserResponse user, TaskResponse task) {
        task.setModifiedBy(recipient.getId());
        task.setSharedBy(user.getLogin());
        final TaskEntity taskEntity = TaskEntity.transform(task);
        taskEntity.setId(null);

        this.taskDao.save(taskEntity);

        try {
            String recipientAddress = recipient.getLogin();
            String userAddress = user.getLogin();
            this.eMailComponent.sendSharedTask(recipientAddress, userAddress, task);
        } catch (RuntimeException e) {
            return;
        }

    }

}
