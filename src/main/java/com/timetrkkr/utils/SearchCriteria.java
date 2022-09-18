package com.timetrkkr.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class SearchCriteria {
    private int monthNumber;
    private int year;
    private List<Long> timeRecordIds;
}
