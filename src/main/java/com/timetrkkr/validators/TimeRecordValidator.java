package com.timetrkkr.validators;

import com.timetrkkr.entities.TimeRecord;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class TimeRecordValidator {

    protected void validateTimeRecord(TimeRecord timeRecord){
        List<String> messages = new ArrayList<>();

        if (timeRecord.getDateLogin().isBefore(LocalDate.now())){
            messages.add("Date login must be current date");
        }

        if (timeRecord.getTimeOut().isBefore(timeRecord.getTimeIn())){
            messages.add("Invalid time out Time:"+ timeRecord.getTimeOut() );
        }

        if (messages.size() > 0){
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, messages.toString()
            );
        }
    }

}
