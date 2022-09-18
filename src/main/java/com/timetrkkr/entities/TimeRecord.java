package com.timetrkkr.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "time_records")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeRecord {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(
            nullable = false,
            updatable = true,
            name = "dateLogin"
    )
    private LocalDate dateLogin  = LocalDate.now();

    @Column(
            nullable = false,
            updatable = true,
            name = "timeIn"
    )
    private LocalTime timeIn = LocalTime.now();

    @Column(
            nullable = false,
            updatable = true,
            name = "timeOut"
    )
    private LocalTime timeOut = LocalTime.now();

    @JsonIgnoreProperties(value = "users", allowSetters = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "userId",
            referencedColumnName = "id"
    )
    private User user;
}
