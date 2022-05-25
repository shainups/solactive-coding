package com.solactive.realtimestatistics.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Statistics {

    @JsonIgnore
    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;

    private Statistics(Builder builder) {
        setSum(builder.sum);
        setAvg(builder.avg);
        setMax(builder.max);
        setMin(builder.min);
        setCount(builder.count);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public static final class Builder {
        private double sum;
        private double avg;
        private double max;
        private double min;
        private long count;

        private Builder() {
        }

        public Builder withSum(double sum) {
            this.sum = sum;
            return this;
        }

        public Builder withAvg(double avg) {
            this.avg = avg;
            return this;
        }

        public Builder withMax(double max) {
            this.max = max;
            return this;
        }

        public Builder withMin(double min) {
            this.min = min;
            return this;
        }

        public Builder withCount(long count) {
            this.count = count;
            return this;
        }

        public Statistics build() {
            return new Statistics(this);
        }
    }
}
