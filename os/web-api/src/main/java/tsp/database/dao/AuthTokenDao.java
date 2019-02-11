package tsp.database.dao;

import tsp.database.entity.AuthTokenEntity;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class AuthTokenDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String HQLGetByToken = "from AuthTokenEntity where token = :token";
    private static final String HQLGetByUserName = "from AuthTokenEntity where userName = :userName";

    public AuthTokenEntity getByToken(String token) {
        Query query = entityManager.createQuery(HQLGetByToken, AuthTokenEntity.class);
        query.setParameter("token", token);
        final List<AuthTokenEntity> result = query.getResultList();
        return result.isEmpty() ? null : result.iterator().next();
    }

    public List<AuthTokenEntity> getByUserName(String userName) {
        Query query = entityManager.createQuery(HQLGetByUserName, AuthTokenEntity.class);
        query.setParameter("userName", userName);
        final List result = query.getResultList();
        return result;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addAuthToken(AuthTokenEntity authToken) {
        entityManager.persist(authToken);
    }

    public void deleteAuthToken(AuthTokenEntity authToken) {
        entityManager.remove(authToken);
    }
}
