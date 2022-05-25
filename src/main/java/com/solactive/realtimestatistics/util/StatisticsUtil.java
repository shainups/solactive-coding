package com.solactive.realtimestatistics.util;

import com.solactive.realtimestatistics.model.Statistics;

public class StatisticsUtil {

    protected StatisticsUtil() {

    }

    public static Statistics cloneStatistics(Statistics statistics) {
        return Statistics.newBuilder().withAvg(statistics.getAvg()).withCount(statistics.getCount())
                .withMax(statistics.getMax()).withMin(statistics.getMin()).withSum(statistics.getSum()).build();
    }

    public static void calculateStatistics(Statistics store, Double price) {
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
            store.setSum(store.getSum() + price);
            store.setAvg(store.getSum() / store.getCount());
        }
    }
}
