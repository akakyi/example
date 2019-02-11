package tsp.services;

import tsp.database.dao.RoleDao;
import tsp.database.entity.RoleEntity;
import tsp.rest.json.response.RoleResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class RoleService {

    @EJB
    private RoleDao roleDao;

    public List<RoleResponse> getAll() {
        return roleDao.list().stream().map(RoleResponse::transform).collect(Collectors.toList());
    }

    public RoleResponse getById(int id) {
        final RoleEntity role = roleDao.getById(id);
        return role == null ? null : RoleResponse.transform(role);
    }

}
