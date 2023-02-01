package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.entities.Role;

import java.util.List;

public interface RoleService {
    List<Role> getRoles();

    void saveRole(Role roleAdmin);

    void removeRoleById(int id);
}
