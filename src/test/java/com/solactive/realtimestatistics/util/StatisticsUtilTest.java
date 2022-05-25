package com.solactive.realtimestatistics.util;

import com.solactive.realtimestatistics.model.Statistics;
import org.junit.Assert;
import org.junit.Test;

public class StatisticsUtilTest {

    @Test
    public void testConstructor() {
        StatisticsUtil util = new StatisticsUtil();
        Assert.assertNotNull("util can not be null", util);
    }

    @Test
    public void testCloneStatistics() {
        Statistics statistics = Statistics.newBuilder().withCount(2).build();
        Statistics clone = StatisticsUtil.cloneStatistics(statistics);
        Assert.assertEquals("Count not matched", statistics.getCount(), clone.getCount());
        Assert.assertEquals(statistics.getSum(), clone.getSum(), 0.0);
        Assert.assertEquals(statistics.getAvg(), clone.getAvg(), 0.0);
        Assert.assertEquals(statistics.getMin(), clone.getMin(), 0.0);
        Assert.assertEquals(statistics.getMax(), clone.getMax(), 0.0);
    }

    @Test
    public void testCalculateStatisticsFirst() {
        Statistics statistics = Statistics.newBuilder().build();
        double price = 1.2;
        StatisticsUtil.calculateStatistics(statistics, price);

        Assert.assertEquals("Count not matched", statistics.getCount(), 1);
        Assert.assertEquals(statistics.getSum(), price, 0.0);
        Assert.assertEquals(statistics.getAvg(), price, 0.0);
        Assert.assertEquals(statistics.getMin(), price, 0.0);
        Assert.assertEquals(statistics.getMax(), price, 0.0);
    }

    @Test
    public void testCalculateStatistics() {
        Statistics statistics = Statistics.newBuilder().withCount(2).withSum(2.4).withAvg(1.2).withMin(1.1).withMax(1.3)
                .build();
        double price = 1.2;
        StatisticsUtil.calculateStatistics(statistics, price);

        Assert.assertEquals("Count not matched", statistics.getCount(), 3);
        Assert.assertEquals(statistics.getSum(), 3.6, 0.0);
        Assert.assertEquals(statistics.getAvg(), 1.2, 0.0);
        Assert.assertEquals(statistics.getMin(), 1.1, 0.0);
        Assert.assertEquals(statistics.getMax(), 1.3, 0.0);
    }

}
