package web.dao;

import web.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoJpa implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("select distinct u from User u inner join fetch u.roles",
                User.class).getResultList();
    }

    @Override
    public User getUserById(Long id) {
        return entityManager
                .createQuery("select u from User u inner join fetch u.roles where u.id = :id", User.class)
                .setParameter("id", id).getSingleResult();
    }

    @Override
    public User getUserByName(String name) {
        return entityManager
                .createQuery("select u from User u inner join fetch u.roles where u.firstName = :name", User.class)
                .setParameter("name", name).getSingleResult();
    }

    @Override
    public User getUserByEmail(String email) {
        return entityManager
                .createQuery("select u from User u inner join fetch u.roles where u.email = :email", User.class)
                .setParameter("email", email).getSingleResult();
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteUserById(Long id) {
        entityManager.createQuery("delete from User u where u.id = :id")
                .setParameter("id", id).executeUpdate();
    }
}