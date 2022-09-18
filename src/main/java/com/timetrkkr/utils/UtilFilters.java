package com.timetrkkr.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class UtilFilters {

    private LocalDate dateFrom = LocalDate.now();
    private LocalDate dateUntil = LocalDate.now();
    private int size = 100;
    private int page = 0;
}
