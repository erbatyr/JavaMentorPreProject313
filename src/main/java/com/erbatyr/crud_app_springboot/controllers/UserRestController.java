package com.erbatyr.crud_app_springboot.controllers;

import com.erbatyr.crud_app_springboot.models.Role;
import com.erbatyr.crud_app_springboot.models.User;
import com.erbatyr.crud_app_springboot.repositories.RoleRepository;
import com.erbatyr.crud_app_springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserRestController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/")
    public List<User> index() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") long id) {
        User user = userRepository.findById(id).get();
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> create(@RequestBody Map<String, String> map) throws URISyntaxException {
        User createdUser = new User();
        createdUser.setEmail(map.get("email"));
        createdUser.setPassword(map.get("password"));
        createdUser.setFirstName(map.get("firstName"));
        createdUser.setLastName(map.get("lastName"));

        Set<Role> rolesSet = new HashSet<>();
        List<String> roles = Arrays.asList(map.get("roles").split(","));
        for (String s: roles) {
            Long i = Long.parseLong(s);
            rolesSet.add(roleRepository.findById(i).get());
        }
        createdUser.setRoles(rolesSet);

        userRepository.save(createdUser);

        if (createdUser == null) {
            return ResponseEntity.notFound().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdUser.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(createdUser);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@RequestBody Map<String, String> map, @PathVariable(value = "id") long id) {
        User updatedUser = userRepository.findById(id).get();
        updatedUser.setEmail(map.get("email"));
        updatedUser.setFirstName(map.get("firstName"));
        updatedUser.setLastName(map.get("lastName"));
        updatedUser.setPassword(map.get("password"));

        Set<Role> rolesSet = new HashSet<>();
        List<String> roles = Arrays.asList(map.get("roles").split(","));
        for (String s: roles) {
            Long i = Long.parseLong(s);
            rolesSet.add(roleRepository.findById(i).get());
        }

        updatedUser.setRoles(rolesSet);

        userRepository.save(updatedUser);

        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedUser);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable(value = "id") long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
