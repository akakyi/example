package tsp.services.auth;

import tsp.database.dao.AuthTokenDao;
import tsp.database.entity.AuthTokenEntity;
import tsp.utils.EncryptSome;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import java.util.List;

@Stateless
public class AuthService {

    @EJB
    private AuthTokenDao authTokenDao;

    public String addToken(String username) {
        String token = EncryptSome.encryptMd5(username.concat("123"));
        authTokenDao.addAuthToken(new AuthTokenEntity(token, username));
        return token;
    }

    public String getNameByToken(String token) {
        try {
            AuthTokenEntity result = authTokenDao.getByToken(token);
            return result == null ? null : result.getUserName();
        } catch (EJBTransactionRolledbackException e) {
            if (e.getCause() instanceof NoResultException) {
                return null;
            } else {
                throw e;
            }
        }
    }

    public void deleteByToken(String token) {
        authTokenDao.deleteAuthToken(authTokenDao.getByToken(token));
    }

    public List<AuthTokenEntity> findByUserName(String username) {
        try {
            List<AuthTokenEntity> result = authTokenDao.getByUserName(username);
            return result;
        } catch (EJBTransactionRolledbackException e) {
            if (e.getCause() instanceof NoResultException) {
                return null;
            } else {
                throw e;
            }
        }
    }

}
