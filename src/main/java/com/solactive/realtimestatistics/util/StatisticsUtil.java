package com.solactive.realtimestatistics.util;

import com.solactive.realtimestatistics.model.Statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StatisticsUtil {

    protected StatisticsUtil() {

    }

    public static Statistics cloneStatistics(Statistics statistics) {
        return Statistics.newBuilder().withAvg(statistics.getAvg()).withCount(statistics.getCount())
                .withMax(statistics.getMax()).withMin(statistics.getMin()).withSum(statistics.getSum()).build();
    }

    public static void calculateStatistics(Statistics store, Double price) {
        BigDecimal bdPrice = BigDecimal.valueOf(price);
        store.setCount(store.getCount() + 1);        
        if (store.getCount() == 1) {
            store.setSum(price);
            store.setAvg(price);
            store.setMin(price);
            store.setMax(price);
        } else {
            if (price < store.getMin()) {
                store.setMin(price);
            }
            if (price > store.getMax()) {
                store.setMax(price);
            }
            BigDecimal sum = BigDecimal.valueOf(store.getSum()).add(bdPrice).setScale(7,
                    RoundingMode.HALF_UP);
            store.setSum(sum.doubleValue());
            store.setAvg(sum.divide(BigDecimal.valueOf(store.getCount()), RoundingMode.HALF_UP).doubleValue());
        }
    }
}
