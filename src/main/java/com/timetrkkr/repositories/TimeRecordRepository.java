package com.timetrkkr.repositories;

import com.timetrkkr.entities.TimeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface TimeRecordRepository extends CrudRepository<TimeRecord, Long>, PagingAndSortingRepository<TimeRecord, Long> {

    @Query(
        "SELECT t FROM TimeRecord t WHERE t.user.id = :userId AND t.id IN ( :timeRecordIds )"
    )
    List<TimeRecord> getAllUserTimeRecords(Long userId, List<Long> timeRecordIds);

    @Query(
        "SELECT t FROM TimeRecord t WHERE t.user.id = :userId AND t.dateLogin = :dateLogin "
    )
    TimeRecord getUserTimeRecordByDay(Long userId, LocalDate dateLogin);

    @Query(
        "SELECT t FROM TimeRecord t WHERE t.user.id = :userId AND (t.dateLogin >= :dateFrom AND t.dateLogin <= :dateUntil)"
    )
    Page<TimeRecord> getUserTimeRecordsByDateRange(Long userId, LocalDate dateFrom, LocalDate dateUntil, Pageable pageable);

    @Query(
        "SELECT t FROM TimeRecord t WHERE t.user.id = :userId AND (t.dateLogin >= :dateFrom AND t.dateLogin <= :dateUntil)"
    )
    List<TimeRecord> getUserComputedTimeRecords(Long userId, LocalDate dateFrom, LocalDate dateUntil);
}
