package tsp.database.dao;

import tsp.database.entity.UserEntity;
import tsp.utils.EncryptSome;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String HQL_GET_ALL = "from UserEntity";
    private static final String HQL_GET_BY_LOGIN = "from UserEntity where login = :login";

    public List<UserEntity> getAll() {
        return entityManager.createQuery(HQL_GET_ALL, UserEntity.class).getResultList();
    }

    public UserEntity getById(int id) {
        return entityManager.find(UserEntity.class, id);
    }

    public UserEntity getByLogin(String login) {
        Query query = entityManager.createQuery(HQL_GET_BY_LOGIN, UserEntity.class);
        query.setParameter("login", login);
        final List resultList = query.getResultList();
        return resultList.isEmpty() ? null : (UserEntity) resultList.iterator().next();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void save(UserEntity entity) {
        entity.setPassword(EncryptSome.encryptMd5(entity.getPassword()));
        entityManager.persist(entity);
        entityManager.flush();
    }

    public void update(UserEntity entity) {
        entityManager.merge(entity);
    }

    public void delete(UserEntity entity) {
        entityManager.remove(entity);
    }

}
