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
    @GetMapping("{id}")
    public String showUserById(@PathVariable("id") int id, Model model){
        model.addAttribute("userById", service.getUserById(id));
        model.addAttribute("id", id);
        model.addAttribute("pageTitle", service.getUserById(id).getUsername());
        return "userById";
    }

    @GetMapping("/{id}/delete")
    public String deleteUserById(@PathVariable("id")int id, Model model){
        service.removeUserById(id);
        return "redirect:/admin";
    }

    @PostMapping("/create")
    public String createNewUser(@ModelAttribute("user") User user,
                                Model model,
                                @RequestParam(value = "roleForNewUser") String roleForNewUser){
        model.addAttribute("roles", roleService.getRoles());
        Role role = new Role(roleForNewUser);
        roleService.saveRole(role);
        user.setRoles(Set.of(role));
        service.saveUser(user);
        return "redirect:/admin";
    }

    @PatchMapping("/{id}/update")
    public String update(@ModelAttribute("user") User user,
                         @PathVariable("id") int id){
        service.updateUser(id, user);
        return "redirect:/admin";
    }
}

