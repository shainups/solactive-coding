package com.solactive.realtimestatistics.service;

import com.solactive.realtimestatistics.dao.StatisticsStore;
import com.solactive.realtimestatistics.model.Statistics;
import com.solactive.realtimestatistics.model.Tick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsStore statisticsStore;

    @Override
    public void processTick(Tick tick, long currTime) {
        statisticsStore.processTick(tick, currTime);
    }

    @Override
    public Statistics getStatistics(long currTime) {
        return statisticsStore.getStatistics(currTime);
    }

    @Override
    public Statistics getStatistics(String instrument, long currTime) {
        return statisticsStore.getStatistics(instrument, currTime);

    }
}
