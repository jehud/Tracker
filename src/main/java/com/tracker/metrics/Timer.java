package com.tracker.metrics;

import com.tracker.persistence.MetricsWriter;
import org.apache.commons.lang.StringUtils;

/**
 *
 *
 *
 */
public class Timer
{
    private String defaultMetricName;
    private long startTime;
    private long endTime;

    public Timer()
    {
        startTime = System.nanoTime();
    }

    public Timer(String metricName)
    {
        this.defaultMetricName = metricName;
    }

    public void start()
    {
       startTime = System.nanoTime();
    }

    public void stop(String metricName)
    {
        validateCounter(metricName);
        endTime = System.nanoTime();
        MetricsWriter.writeTimerMetric(metricName, startTime, endTime);
    }

    public void stop()
    {
       validateCounter();
       endTime = System.nanoTime();
       MetricsWriter.writeTimerMetric(defaultMetricName, startTime, endTime);
    }

    public void setMetricName(String metricName)
    {
        this.defaultMetricName = metricName;
    }

    public String getMetricName()
    {
        return defaultMetricName;
    }

    private void validateCounter()
    {
        validateCounter(defaultMetricName);
    }

    private static void validateCounter(String metricName)
    {
        if (StringUtils.isBlank(metricName))
        {
            throw new IllegalArgumentException("An empty counter name was specified, you must specify a metric name");
        }
    }

}
