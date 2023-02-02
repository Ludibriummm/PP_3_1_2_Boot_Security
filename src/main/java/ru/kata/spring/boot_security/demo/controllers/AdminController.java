package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService service;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
    }
    @GetMapping
    public String pageForAdmins(Model model){
        model.addAttribute("users", service.getAllUsers());
        return "admin";
    }
    @GetMapping("/new")
    public String getUserCreateForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.getRoles());
        return "new_user";
    }

    @PostMapping("/createNew")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "my_roles") String stringRole) {
        service.createUser(user, stringRole);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/delete")
    public String deleteUserById(@PathVariable("id")int id){
        service.removeUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String showUserById(@PathVariable("id") int id, Model model){
        model.addAttribute("userById", service.getUserById(id));
        model.addAttribute("id", id);
        model.addAttribute("pageTitle", service.getUserById(id).getUsername());
        model.addAttribute("roles", roleService.getRoles());
        return "userById";
    }

    @PostMapping(value = "/{id}/update")
    public String updateUser(@ModelAttribute("userById") User user, @PathVariable("id") int id) {
        user.setPassword(user.getPassword());
        service.updateUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}/delete")
    public String removeUserById(@PathVariable("id") int id) {
        roleService.removeRoleById(id);
        service.removeUserById(id);
        return "redirect:/admin";
    }
}

