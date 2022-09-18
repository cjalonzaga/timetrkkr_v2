package com.timetrkkr.utils;

import com.timetrkkr.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class ComputedTimeRecords {
    private double totalTime = 0.0;
    private double totalExcessTime = 0.0;
    private User user;
}
