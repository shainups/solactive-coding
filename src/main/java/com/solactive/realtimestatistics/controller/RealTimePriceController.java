package com.solactive.realtimestatistics.controller;

import com.solactive.realtimestatistics.model.Statistics;
import com.solactive.realtimestatistics.service.StatisticsService;
import com.solactive.realtimestatistics.model.Tick;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@RestController
@Slf4j
public class RealTimePriceController {

    @Autowired
    private StatisticsService statisticsService;

    @PostMapping("/ticks")
    public ResponseEntity<?> receiveTick(@Validated @RequestBody Tick tick){
        long start = System.currentTimeMillis();
        long currTime = Instant.now(Clock.systemUTC()).getEpochSecond();
        long tickTime = Instant.ofEpochMilli(tick.getTimestamp()).atOffset(ZoneOffset.UTC).toEpochSecond();
        log.debug("tick::{}|tickTime:{}|now:{}", tick.toString(),tickTime, currTime);
        if (currTime - tickTime > 60) {
            log.debug("Expired request:{}", start);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        // Resetting the epoch time in second
        tick.setTimestamp(tickTime);
        statisticsService.processTick(tick, currTime);
        log.info("TT:{}", (System.currentTimeMillis() - start));
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @GetMapping("/statistics")
    public Statistics getStatistics(){
        long currTime = Instant.now(Clock.systemUTC()).getEpochSecond();
        log.debug("Get statistics on {}", currTime);
        return statisticsService.getStatistics(currTime);
    }

    @GetMapping("/statistics/{instrumentIdentifier}")
    public Statistics getInstrumentStatistics(@PathVariable String instrumentIdentifier){
        long currTime = Instant.now(Clock.systemUTC()).getEpochSecond();
        log.debug("Get statistics on {}|instrumentIdentifier:{}", currTime, instrumentIdentifier);
        return statisticsService.getStatistics(instrumentIdentifier, currTime);
    }
}
