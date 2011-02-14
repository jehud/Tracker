package com.tracker.metrics;

import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: jtruelove
 * Date: Feb 13, 2011
 * Time: 5:07:27 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Counter
{
    private String defaultMetricName;

    public Counter()
    {
    }

    public Counter(String metricName)
    {
        this.defaultMetricName = metricName;
    }

    public Counter(String metricName, long count)
    {
        this.defaultMetricName = metricName;
        count(count);
    }

    public void countOne()
    {
        validateCounter(defaultMetricName);
        MetricsWriter.writeCountMetric(defaultMetricName, 1);
    }

    public void countOne(String metricName)
    {
        validateCounter(metricName);
        MetricsWriter.writeCountMetric(metricName, 1);
    }

    public void count(String metricName, long count)
    {
        validateCounter(metricName);
        MetricsWriter.writeCountMetric(metricName, count);
    }

    public void count(long count)
    {
        validateCounter();
        MetricsWriter.writeCountMetric(defaultMetricName, count);
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
        if(StringUtils.isBlank(metricName))
        {
           throw new IllegalArgumentException("An empty counter name was specified, you must specify a metric name");
        }
    }

}

