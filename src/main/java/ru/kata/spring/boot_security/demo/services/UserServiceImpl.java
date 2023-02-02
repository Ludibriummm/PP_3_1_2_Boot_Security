package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;


import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
    }
    public User findByUsername(String username){
        return userRepository.findByUsername(username); //по умолчанию Optional
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    @Override
    public User getUserById(int id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            return userById.get();
        } else {
            throw new UsernameNotFoundException(String.format("User with %s not found", id));
        }
    }
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    public void updateUser(int id, User user) {
        Optional<User> userToUpdate = userRepository.findById(id);
        if (userToUpdate.isPresent()) {
            User updatedUser = userToUpdate.get();
            updatedUser.setUsername(user.getUsername());
            updatedUser.setEmail(user.getEmail());
            userRepository.save(updatedUser);
        } else {
            throw new UsernameNotFoundException(String.format("User %s with %s not found", user, id));
        }
    }
    @Override
    public void removeUserById(int id) { userRepository.deleteById(id);
    }

    public void registration(User user){
        Role role = new Role("ROLE_USER");
        roleService.saveRole(role);
        user.setRoles(Set.of(role));
        saveUser(user);
    }

    @Override
    public void createUser(User user, String stringRole) {
        Role role = new Role(stringRole);
        roleService.saveRole(role);
        user.setRoles(Set.of(role));
        saveUser(user);
    }
}
