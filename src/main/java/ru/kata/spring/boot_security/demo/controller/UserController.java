package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.security.MyUserDetails;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.util.List;
import java.util.Set;


@Controller
public class UserController {
    private final UserServiceImp userService;
    private final RoleRepository roleRepository;

    public UserController(RoleRepository roleRepository, UserServiceImp userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @GetMapping("/user")
    public String showUserInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        String username = user.getUsername();
        String lastname = user.getLastname();
        Set<Role> roles = user.getRoles();

        model.addAttribute("username", username);
        model.addAttribute("lastname", lastname);
        model.addAttribute("userRoles", roles);

        return "user";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String showAll(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("AllUsers", users);
        return "admin";
    }
    @GetMapping("/updateInfo")
    public String updateUser(@RequestParam("userId") Long userId, Model model) {
        User user = userService.getUser(userId);
        List<Role> allRoles = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("userId", userId);
        model.addAttribute("allRoles", allRoles);
        return "update";
    }

}

