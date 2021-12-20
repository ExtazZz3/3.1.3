package web.dao;

import org.springframework.stereotype.Repository;
import web.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoJpa implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveRole(Role role) {
        if (role.getId() == null) {
            entityManager.persist(role);
        } else {
            entityManager.merge(role);
        }
    }

    @Override
    public Role getRoleById(Long id) {
        return entityManager
                .createQuery("select r from Role r where r.id = :id", Role.class)
                .setParameter("id", id).getSingleResult();
    }

    @Override
    public Role getRoleByType(String type) {
        return entityManager
                .createQuery("select r from Role r where r.role = :type", Role.class)
                .setParameter("type", type).getSingleResult();
    }

    @Override
    public List<Role> getAllRoles() {
        return entityManager.createQuery("select r from Role r", Role.class).getResultList();
    }
}