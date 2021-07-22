package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserResource {

    @Autowired
    private UserDaoService userService;

    // GET /users
    // retrieveAllUsers
    @GetMapping(path = "/users")
    public List<User> retrieveAllUsers() {
        return userService.findAll();
    }

    // GET /users/{id}
    // retrieveUser(int id)
    @GetMapping(path = "/users/{id}")
    public EntityModel<User> retrieveAllUsers(@PathVariable Integer id) {
        User user = userService.findOne(id);
        if(user == null) {
            throw new UserNotFoundException("id " + id + " Not found");
        }
        EntityModel<User> model = EntityModel.of(user);
        // create a link named "all-users" (to get all users) attached to User returned object
        WebMvcLinkBuilder linkToUsers = linkTo((methodOn(this.getClass()).retrieveAllUsers()));
        model.add(linkToUsers.withRel("all-users"));
        return model;
    }

    // input details of user
    // output = CREATED & Return the created URI
    @PostMapping(path = "/users")
    public ResponseEntity createUser(@Valid @RequestBody User user) {
        User savedUser = userService.save(user);
        // /users/{id} - savedUser.getId()
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@PathVariable Integer id) {
        User user = userService.deleteById(id);
        if(user == null) {
            throw new UserNotFoundException("id " + id + " Not Found !");
        }
    }
}
