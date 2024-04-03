package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.User;

@Component
public class UserValidator implements Validator {
    private final UserServiceImp userServiceImp;

    @Autowired
    public UserValidator(UserServiceImp userServiceImp) {
        this.userServiceImp = userServiceImp;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userServiceImp.existsByUsername(user.getUsername())) {
            errors.rejectValue("username", "", "User exists");
        }
    }

}

