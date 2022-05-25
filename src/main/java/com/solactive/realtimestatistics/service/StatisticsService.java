package com.solactive.realtimestatistics.service;

import com.solactive.realtimestatistics.model.Statistics;
import com.solactive.realtimestatistics.model.Tick;

public interface StatisticsService {
    void processTick(Tick tick, long currTime);
    Statistics getStatistics(long currTime);
    Statistics getStatistics(String instrument, long currTime);
}
