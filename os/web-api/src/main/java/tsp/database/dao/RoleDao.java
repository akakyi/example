package tsp.database.dao;


import tsp.database.entity.RoleEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class RoleDao {

    @PersistenceContext
    private EntityManager entityManager;


    private static final String HQL_GET_ALL = "select role from RoleEntity role";

    /**
     * Вернёт список всех сущностей в этой таблице.
     * @return Список сущностей.
     */
    public List<RoleEntity> list() {
        return entityManager.createQuery(HQL_GET_ALL, RoleEntity.class).getResultList();
    }

    /**
     * Вернёт сущность по её id.
     * @param id id сущности в бд.
     * @return Объект сущности.
     */
    public RoleEntity getById(int id) {
        return entityManager.find(RoleEntity.class, id);
    }

    public void delete(RoleEntity entity) {
        entityManager.remove(entity);
    }
}
