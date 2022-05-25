package com.solactive.realtimestatistics.dao;

import com.solactive.realtimestatistics.model.Statistics;
import com.solactive.realtimestatistics.model.Tick;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;

@RunWith(SpringRunner.class)
public class StatisticsStoreImplTest {

    @Mock
    private ScheduledExecutorService scheduledExecutor;

    @InjectMocks
    private StatisticsStoreImpl statisticsStore;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessTick() throws InterruptedException {
        long currTime = Instant.now(Clock.systemUTC()).getEpochSecond();
        Statistics oldStat = statisticsStore.getStatistics(currTime);
        Tick tick = createTick("abc", 1.2, currTime - 50);
        statisticsStore.processTick(tick, currTime);
        Statistics newStat = statisticsStore.getStatistics(currTime);
        Assert.assertEquals("Count not matched", oldStat.getCount() + 1, newStat.getCount());
    }

    private Tick createTick(String instrument, double amount, long timestamp) {
        Tick tick = new Tick();
        tick.setInstrument(instrument);
        tick.setPrice(amount);
        tick.setTimestamp(timestamp);
        return tick;
    }
}
