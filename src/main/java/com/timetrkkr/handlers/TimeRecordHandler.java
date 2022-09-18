package com.timetrkkr.handlers;

import com.timetrkkr.entities.TimeRecord;
import com.timetrkkr.services.TimeRecordService;
import com.timetrkkr.utils.ComputedTimeRecords;
import com.timetrkkr.utils.SearchCriteria;
import com.timetrkkr.utils.UtilFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class TimeRecordHandler {
    @Autowired
    private TimeRecordService timeRecordService;

    @PostMapping("time-records/{userId}")
    public TimeRecord createTimeRecord(@PathVariable("userId") Long userId, @RequestBody TimeRecord timeRecord){
        return timeRecordService.createTimeRecord(userId , timeRecord);
    }

    @GetMapping("time-records/{userId}")
    public List<TimeRecord> getTimeRecordsByUser(@PathVariable("userId") Long userId, @RequestBody(required = false) SearchCriteria body){
        return  timeRecordService.getAllTimeRecordByUser(userId , body);
    }

    @PutMapping("time-records/{userId}")
    public TimeRecord logOutUser(@PathVariable("userId") Long userId, @RequestBody TimeRecord timeRecord){
        return timeRecordService.logOutUser(userId , timeRecord);
    }

    //get time record given date range
    @GetMapping("time-records/{userId}/getBy-dateRange")
    public List<TimeRecord> getUserTimeRecordByDateRange(@PathVariable("userId") Long userId, @RequestBody UtilFilters body){
        return timeRecordService.getUserTimeRecordsByDateRange(userId, body);
    }

    @GetMapping("time-records/{userId}/get-computedRecords")
    public ComputedTimeRecords getUserComputedTimeRecords(
            @PathVariable("userId") Long userId, @RequestBody UtilFilters body
    ){
        return timeRecordService.getUserComputedTimeRecords(userId, body);
    }
}
