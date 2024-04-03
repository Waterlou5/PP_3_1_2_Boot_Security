package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;
import ru.kata.spring.boot_security.demo.service.UserValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final UserValidator userValidator;
    private final RoleRepository roleRepository;
    @Autowired
    private UserServiceImp userService;

    @Autowired
    public AuthController(RegistrationService registrationService, UserValidator userValidator, RoleRepository roleRepository) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user, Model model) {
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String submitRegistration(@ModelAttribute("user") @Valid User user,
                                     @RequestParam(value = "userId", required = false) Long userId,
                                     BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        if (userId != null) {
            User existingUser = userService.getUser(userId);
            if (existingUser != null) {
                user.setId(existingUser.getId());
            }
        }
        registrationService.register(user);
        if (userId != null) {
            return "redirect:/admin";
        } else {
            return "redirect:/auth/login";
        }
    }

}
