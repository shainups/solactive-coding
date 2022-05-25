package com.solactive.realtimestatistics.dao;

import com.solactive.realtimestatistics.model.Statistics;
import com.solactive.realtimestatistics.util.StatisticsUtil;
import com.solactive.realtimestatistics.model.Tick;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class StatisticsStoreImpl implements StatisticsStore {

    // Statistics for all instruments (Thread safe)
    private Map<Long, Statistics> mapAllStatistics = new ConcurrentHashMap<>();

    // Statistics for specific instruments
    private Map<String, Map<Long, Statistics>> mapInstrumentStatistics = new ConcurrentHashMap<>();

    @Override
    public void processTick(Tick tick, long currTime) {
        long start = currTime - 60;
        long tickTime = tick.getTimestamp();
        // Iterate from 60 sec back to the tick time
        try {
            for (Long i = start; i <= tickTime; i++) {
                // Calculate overall statistics
                Statistics statistics = mapAllStatistics.computeIfAbsent(i, key -> Statistics.newBuilder().build());
                StatisticsUtil.calculateStatistics(statistics, tick.getPrice());
                log.debug("Time:{}|sum:{}|count:{}|avg:{}", i, statistics.getSum(), statistics.getCount(), statistics.getAvg());
                // Process Instrument statistics
                Map<Long, Statistics> mapInstrumentStats = mapInstrumentStatistics.computeIfAbsent(tick.getInstrument(), key -> new ConcurrentHashMap<>());
                Statistics instStatistics = mapInstrumentStats.computeIfAbsent(i, key -> Statistics.newBuilder().build());
                StatisticsUtil.calculateStatistics(instStatistics, tick.getPrice());
            }
        } catch (Exception e) {
            log.error("Exception processing tick.", e);
        }
    }

    @Override
    public Statistics getStatistics(long currTime) {
        log.debug("getStatistics currTime:{}|key:{}", currTime, currTime - 60);
        Statistics statistics = mapAllStatistics.get(currTime - 60);
        if (null == statistics) {
            return Statistics.newBuilder().build();
        }
        return StatisticsUtil.cloneStatistics(statistics);
    }

    @Override
    public Statistics getStatistics(String instrument, long currTime) {
        log.debug("getStatistics currTime:{}|key:{}|instrument:{}", currTime, currTime - 60, instrument);
        Map<Long, Statistics> instrumentStatistics = mapInstrumentStatistics.get(instrument);
        if (null == instrumentStatistics) {
            return Statistics.newBuilder().build();
        }
        Statistics statistics = instrumentStatistics.get(currTime - 60);
        if (null == statistics) {
            return Statistics.newBuilder().build();
        }
        return StatisticsUtil.cloneStatistics(statistics);
    }

    @Scheduled(cron = "* * * * * *")
    protected void cleanupOlderEntries() {
        long currTime = Instant.now(Clock.systemUTC()).getEpochSecond();
        long start = currTime - 60;
        mapAllStatistics.entrySet().removeIf(key -> key.getKey() < start);
        mapInstrumentStatistics.entrySet().stream().forEach(e -> e.getValue().entrySet().removeIf(key -> key.getKey() < start));
    }
}
