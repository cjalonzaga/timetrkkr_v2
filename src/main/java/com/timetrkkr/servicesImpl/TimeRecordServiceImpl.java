package com.timetrkkr.servicesImpl;

import com.timetrkkr.entities.TimeRecord;
import com.timetrkkr.entities.User;
import com.timetrkkr.repositories.TimeRecordRepository;
import com.timetrkkr.repositories.UserRepository;
import com.timetrkkr.services.TimeRecordService;
import com.timetrkkr.utils.ComputedTimeRecords;
import com.timetrkkr.utils.SearchCriteria;
import com.timetrkkr.utils.UtilFilters;
import com.timetrkkr.validators.TimeRecordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Pageable;

import java.math.RoundingMode;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
public class TimeRecordServiceImpl extends TimeRecordValidator implements TimeRecordService {

    @Autowired
    private TimeRecordRepository timeRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TimeRecord createTimeRecord(Long userId, TimeRecord timeRecord) {

        User user = getUserById(userId);

        if ( userRepository.ifUserAlreadyLogin ( user.getId(), timeRecord.getDateLogin() ) ){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "User have already login!"
            );
        }

        validateTimeRecord(timeRecord);

        TimeRecord userTimeRecord = new TimeRecord();
        userTimeRecord.setDateLogin( timeRecord.getDateLogin() );
        userTimeRecord.setUser(user);

        return timeRecordRepository.save( userTimeRecord );
    }

    @Override
    public List<TimeRecord> getAllTimeRecordByUser(Long userId, SearchCriteria body) {
        //method to get user time records base on given time record ids
        User user = getUserById(userId);

        if (body == null){
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY, "Please provide a Time Record ids"
            );
        }

        List<TimeRecord> timeRecordList = timeRecordRepository.getAllUserTimeRecords( user.getId() , body.getTimeRecordIds() );

        if ( timeRecordList.size() != body.getTimeRecordIds().size() ){
            List<Long> userTimeRecordsIds = timeRecordList.stream().map(
                TimeRecord::getId ).collect(Collectors.toList()
            );

            List<Long> missingIds = body.getTimeRecordIds().stream().filter( ids->
                !userTimeRecordsIds.contains(ids) ).collect(Collectors.toList()
            );

            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY , "The user does not have particular time record Ids: "+ missingIds
            );
        }

        return timeRecordList;
    }

    @Override
    public TimeRecord logOutUser(Long userId, TimeRecord timeRecord) {
        User user = getUserById(userId);

        TimeRecord userTimeRecord = timeRecordRepository.getUserTimeRecordByDay( user.getId() , timeRecord.getDateLogin() );

        if (userTimeRecord == null){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND , "TimeRecord not found given date login "+ timeRecord.getDateLogin()
            );
        }

        userTimeRecord.setTimeOut(timeRecord.getTimeOut());
        validateTimeRecord(userTimeRecord);

        return timeRecordRepository.save(userTimeRecord);
    }

    @Override
    public void deleteUserTimeRecord(Long userId, SearchCriteria body) {
        User user = getUserById(userId);
        List<TimeRecord> timeRecordList = timeRecordRepository.getAllUserTimeRecords( user.getId() , body.getTimeRecordIds() );

        if ( timeRecordList.size() != body.getTimeRecordIds().size() ){
            List<Long> userTimeRecordsIds = timeRecordList.stream().map(
                TimeRecord::getId ).collect(Collectors.toList()
            );

            List<Long> missingIds = body.getTimeRecordIds().stream().filter( ids->
                !userTimeRecordsIds.contains(ids) ).collect(Collectors.toList()
            );

            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY , "The user does not have particular time record Ids: "+ missingIds
            );
        }

        timeRecordRepository.deleteAllById( body.getTimeRecordIds() );
    }

    //method to get all user time records given date range
    @Override
    public List<TimeRecord> getUserTimeRecordsByDateRange(Long userId, UtilFilters body) {
        User user = getUserById(userId);

        if (body == null){
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY, "Empty request body"
            );
        }

        if (body.getDateUntil().isBefore(body.getDateFrom())){
            throw new ResponseStatusException(
              HttpStatus.UNPROCESSABLE_ENTITY, "Invalid date range"
            );
        }
        UtilFilters defaultFilters = new UtilFilters();
        int size = (body.getSize() < 1) ? defaultFilters.getSize() : body.getSize();
        int page = (body.getPage() < 0) ? defaultFilters.getPage() : body.getPage();

        Pageable pageable = PageRequest.of(page, size);

        return timeRecordRepository.getUserTimeRecordsByDateRange(
            user.getId(), body.getDateFrom(), body.getDateUntil(), pageable
        ).toList();
    }

    @Override
    public ComputedTimeRecords getUserComputedTimeRecords(Long userId, UtilFilters body) {
        User user = getUserById(userId);

        if (body == null){
            throw new ResponseStatusException(
              HttpStatus.UNPROCESSABLE_ENTITY, "Please provide a date range"
            );
        }

        if (body.getDateUntil().isBefore(body.getDateFrom())){
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, "Invalid given date range"
            );
        }
        List<TimeRecord> timeRecordList = timeRecordRepository.getUserComputedTimeRecords(
          user.getId(), body.getDateFrom(), body.getDateUntil()
        );

        double totalTime = timeRecordList.stream().mapToDouble( (TimeRecord) ->
            Duration.between(TimeRecord.getTimeIn(), TimeRecord.getTimeOut()).toMinutes()
            //TimeRecord.getTimeIn().getMinute() + TimeRecord.getTimeOut().getMinute()
        ).sum() / 60.0;

        List<TimeRecord> excessTimeRecordList = timeRecordList.stream().filter( (TimeRecord) ->
           Duration.between(TimeRecord.getTimeIn(), TimeRecord.getTimeOut()).toMinutes() > 480.0
        ).collect(Collectors.toList());

        double totalExcessTime = excessTimeRecordList.stream().mapToDouble( (TimeRecord)->
               Duration.between(TimeRecord.getTimeIn(), TimeRecord.getTimeOut()).toMinutes() - 480.0
        ).sum() / 60.0;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);

        return new ComputedTimeRecords(
            Double.parseDouble(decimalFormat.format(totalTime)),
            Double.parseDouble(decimalFormat.format(totalExcessTime)),
            user
        );
    }


    private User getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow( ()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User with id: "+userId+" not found!" )
        );
    }
}
