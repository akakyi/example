package tsp.database.dao;

import tsp.database.entity.CategoryEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String HQL_GET_ALL = "select category from CategoryEntity category";

    private static final String HQL_GET_BY_USER_ID = "from CategoryEntity where modified_by = :userId";


    public List<CategoryEntity> list() {
        return entityManager.createQuery(HQL_GET_ALL, CategoryEntity.class).getResultList();
    }

    public CategoryEntity getById(int id) {
        return entityManager.find(CategoryEntity.class, id);
    }

    public List<CategoryEntity> getByUserId(int id) {
        Query query = entityManager.createQuery(HQL_GET_BY_USER_ID, CategoryEntity.class);
        query.setParameter("userId", id);
        final List resultList = query.getResultList();
        return resultList;
    }

    public void save(CategoryEntity entity) {
        entityManager.persist(entity);
    }

    public void update(CategoryEntity entity) {
        entityManager.merge(entity);
    }

    public void delete(CategoryEntity entity) {
        entityManager.remove(entity);
    }
}

