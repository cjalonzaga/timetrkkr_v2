package com.timetrkkr.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(
            nullable = false,
            updatable = true,
            name = "firstName"
    )
    private String firstName;

    @Column(
            nullable = false,
            updatable = true,
            name = "lastName"
    )
    private String lastName;

    @Column(
            nullable = false,
            updatable = true,
            unique =  true,
            name = "email"
    )
    private String email;

    @Column(
            nullable = false,
            updatable = true,
            name = "isActive"
    )
    private boolean isActive = true;

    @Column(
            nullable = false,
            updatable = true,
            name = "department"
    )
    private int department = 1;

    @Column(
            nullable = false,
            updatable = false,
            name = "dateAdded"
    )
    private LocalDate dateAdded = LocalDate.now();

    @JsonIgnoreProperties(value = "user" , allowSetters = true)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<TimeRecord> timeRecord;
}
