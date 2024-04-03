package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    public boolean existsByUsername(String username);
    public List<User> getAll();
    public void add(User user);
    public User getUser(Long id);
    public void deleteUser(Long id);
}
