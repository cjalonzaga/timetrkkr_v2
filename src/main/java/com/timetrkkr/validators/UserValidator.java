package com.timetrkkr.validators;

import com.timetrkkr.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class UserValidator {

    public void validate(User user) {

        List<String> messages = new ArrayList<>();

        if (Objects.equals(user.getFirstName(), "")){
            messages.add("Firstname is empty");
        }

        if (Objects.equals(user.getLastName() , "")){
            messages.add("LastName is empty");
        }

        if (Objects.equals(user.getEmail(), "")){
            messages.add("Email is empty");
        }

        if (messages.size() > 0){
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY , messages.toString()
            );
        }
    }

}
