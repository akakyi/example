package tsp.database.dao;

import tsp.database.entity.TaskEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;
import java.util.Date;
import java.util.List;

@Stateless
public class TaskDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String HQL_GET_ALL = "from TaskEntity";

    private static final String HQL_GET_BY_USER = "from TaskEntity where modified_by = :userId";

    private static final String HQL_GET_BY_CATEGORY = "from TaskEntity where category = :categoryId";

    private static final String HQL_GET_BETWEEN = "from TaskEntity where doBefore is not null and doBefore >= "
        + ":startDate and doBefore < :endDate";

    public List<TaskEntity> getBetween(Date startDate, Date endDate) {
        Query query = entityManager.createQuery(HQL_GET_BETWEEN, TaskEntity.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        final List result = query.getResultList();
        return result;
    }

    /**
     * Вернёт список всех сущностей в этой таблице.
     * @return Список сущностей.
     */
    public List<TaskEntity> getAll() {
        return entityManager.createQuery(HQL_GET_ALL, TaskEntity.class).getResultList();
    }

    /**
     * Вернёт сущность по её id.
     * @param id id сущности в бд.
     * @return Объект сущности.
     */
    public TaskEntity getById(int id) {
        return entityManager.find(TaskEntity.class, id);
    }

    /**
     * Вернёт список сущностей, поле modified_by которых совпадает с данным userId.
     * @param userId id пользователя, создавшего/модифицировавшего этот task.
     * @return список сущностей.
     */
    public List<TaskEntity> findByUser(int userId) {
        Query query = entityManager.createQuery(HQL_GET_BY_USER, TaskEntity.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * Сохранит данную сущность в бд.
     * @param entity сохраняемая сущность.
     * @throws EntityExistsException if the entity already exists.
     * (If the entity already exists, the <code>EntityExistsException</code> may
     * be thrown when the persist operation is invoked, or the
     * <code>EntityExistsException</code> or another <code>PersistenceException</code> may be
     * thrown at flush or commit time.)
     * @throws IllegalArgumentException if the instance is not an
     *         entity
     * @throws TransactionRequiredException if there is no transaction when
     *         invoked on a container-managed entity manager of that is of type
     *         <code>PersistenceContextType.TRANSACTION</code>
     */
    public void save(TaskEntity entity) {
        entity.setModifiedAt(new Date());
        entityManager.persist(entity);
    }

    /**
     * Обновит данную сущность в бд.
     * @param entity обновляемая сущность.
     */
    public void update(TaskEntity entity) {
        entityManager.merge(entity);
    }

    public List<TaskEntity> findByCategory(int category) {
        Query query = entityManager.createQuery(HQL_GET_BY_CATEGORY, TaskEntity.class);
        query.setParameter("categoryId", category);
        return query.getResultList();
    }

    public void delete(TaskEntity entity) {
        entityManager.remove(entity);
    }

}
