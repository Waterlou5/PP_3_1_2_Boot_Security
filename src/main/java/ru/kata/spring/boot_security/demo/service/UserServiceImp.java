package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.repositories.UserRepositories;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.MyUserDetails;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Service
public class UserServiceImp implements UserDetailsService {

    @PersistenceContext
    @Autowired
    private EntityManager entityManager;

    private final UserRepositories userRepositories;

    @Autowired
    public UserServiceImp(UserRepositories userDao) {
        this.userRepositories = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepositories.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("user not found");
        }
        return new MyUserDetails(user.get());
    }

    @Transactional
    public boolean existsByUsername(String username) {
        return userRepositories.existsByUsername(username);
    }

    @Transactional
    public List<User> getAll() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Transactional
    public void add(User user) {
        entityManager.contains(user);
        entityManager.merge(user);

    }

    @Transactional
    public User getUser(Long id) {
        return entityManager.find(User.class, id);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }
}
