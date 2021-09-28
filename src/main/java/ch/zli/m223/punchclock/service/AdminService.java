package ch.zli.m223.punchclock.service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    @Transactional
    public User getUserByName(String username) {
        var query = entityManager.createQuery("FROM User WHERE accountname = :username").setParameter("username", username);

        try {
            return (User) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
