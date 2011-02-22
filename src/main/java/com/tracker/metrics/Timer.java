package com.tracker.metrics;

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
        MetricsWriterFactory.getWriter().writeTimerMetric(metricName, (endTime - startTime) / 1000000);
    }

    public void stop()
    {
       validateCounter();
       endTime = System.nanoTime();
       MetricsWriterFactory.getWriter().writeTimerMetric(defaultMetricName, (endTime - startTime) / 1000000);
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
