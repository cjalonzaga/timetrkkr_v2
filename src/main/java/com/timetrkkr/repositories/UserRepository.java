package com.timetrkkr.repositories;

import com.timetrkkr.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query(
        "SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName"
    )
    Boolean ifUserExist(String firstName , String lastName);

    @Query(
        "SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.email = :email "
    )
    Boolean ifEmailExist(String email);

    @Query(
            "SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u JOIN u.timeRecord as time_record "+
            "WITH time_record.dateLogin = :dateLogin AND u.id = :userId"
    )
    Boolean ifUserAlreadyLogin(Long userId, LocalDate dateLogin );
}
