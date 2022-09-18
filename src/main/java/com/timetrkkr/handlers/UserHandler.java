package com.timetrkkr.handlers;

import com.timetrkkr.entities.User;
import com.timetrkkr.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class UserHandler {

    @Autowired
    private UserService userService;

    @PostMapping("users")
    public User create(@RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping("users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("users/{userId}")
    public User getUserById(@PathVariable("userId") Long userId){
        return userService.getUserById(userId);
    }

    @PutMapping("users/{userId}")
    public User updateUser(@PathVariable("userId") Long userId , @RequestBody User user){
       return userService.updateUser(userId , user);
    }

    @DeleteMapping("users/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
    }

}
