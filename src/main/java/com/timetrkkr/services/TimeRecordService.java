package com.timetrkkr.services;

import com.timetrkkr.entities.TimeRecord;
import com.timetrkkr.utils.ComputedTimeRecords;
import com.timetrkkr.utils.SearchCriteria;
import com.timetrkkr.utils.UtilFilters;
import org.apache.tomcat.jni.Time;

import java.time.LocalDate;
import java.util.List;

public interface TimeRecordService {

    TimeRecord createTimeRecord(Long userId , TimeRecord timeRecord);

    List<TimeRecord> getAllTimeRecordByUser(Long userId, SearchCriteria body);

    TimeRecord logOutUser(Long userId , TimeRecord timeRecord);

    void deleteUserTimeRecord(Long userId , SearchCriteria body);

    List<TimeRecord> getUserTimeRecordsByDateRange(Long userId, UtilFilters body);

    ComputedTimeRecords getUserComputedTimeRecords(Long userId, UtilFilters body);
}
