package ch.zli.m223.punchclock.service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.zli.m223.punchclock.domain.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.List;

@ApplicationScoped
public class AdminService {
    
    @Inject
    private EntityManager entityManager;

    public AdminService() { }

    @Transactional
    public User createUser(User user) {
        entityManager.persist(user);
        return user;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<User> getAllUsers() {
        var query = entityManager.createQuery("FROM User");
        return query.getResultList();
    }

    @Transactional
    public boolean deleteUser(Long id) {
        User user = entityManager.find(User.class, id);

        if (null == user) {
            return false;
        }

        entityManager.remove(user);
        return true;
    }

    @Transactional
    public User updateUser(User user) {
        return entityManager.merge(user);
    }
}
