package ru.kata.spring.boot_security.demo.repositories;

import ru.kata.spring.boot_security.demo.models.User;

public interface RegistrationService {
    public void register(User user);
}
