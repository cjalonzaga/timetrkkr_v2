package com.timetrkkr.servicesImpl;

import com.timetrkkr.entities.User;
import com.timetrkkr.repositories.UserRepository;
import com.timetrkkr.services.UserService;
import com.timetrkkr.utils.Departments;
import com.timetrkkr.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@Service
public class UserServiceImpl extends UserValidator implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {

        validate(user);

//        if (userRepository.ifUserExist(user.getFirstName() , user.getLastName())){
//            throw new ResponseStatusException(HttpStatus.CONFLICT , "User already exist");
//        }

        if (userRepository.ifEmailExist(user.getEmail())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT , "Email already exist"
            );
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow( ()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User with id: "+id+" not found!" )
        );
    }

    @Override
    public User updateUser(Long id, User user) {
        User oldUser = getUserById(id);

        if (userRepository.ifUserExist(user.getFirstName() , user.getLastName())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT ,
                    "User with first name: ${user.firstName} and lastname: ${user.lastName} already exist"
            );
        }

        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());

        return userRepository.save( oldUser );
    }

    @Override
    public void deleteUser(Long id) {
        User oldUser = getUserById(id);
        userRepository.deleteById(oldUser.getId());
    }
}
