package com.tracker.metrics;

import org.apache.commons.lang.StringUtils;

/**
 * Represents a basic count metric,
 *
 * i.e. 1 Order was processed, 1 error occured, 5 items were added
 *
 * User: jtruelove
 * Date: Feb 13, 2011
 * Time: 5:07:27 PM
 */
public final class Counter
{
    private String defaultMetricName;

    public Counter()
    {
    }

    public Counter(final String metricName)
    {
        this.defaultMetricName = metricName;
    }

    public Counter(final String metricName, final long count)
    {
        this.defaultMetricName = metricName;
        count(count);
    }

    public void countOne()
    {
        validateCounter();
        MetricsWriterFactory.getWriter().writeCountMetric(defaultMetricName, 1);
    }

    public void countOne(final String metricName)
    {
        validateCounter(metricName);
        MetricsWriterFactory.getWriter().writeCountMetric(metricName, 1);
    }

    public void count(final String metricName, final long count)
    {
        validateCounter(metricName, count);
        MetricsWriterFactory.getWriter().writeCountMetric(metricName, count);
    }

    public void count(final long count)
    {
        validateCounter(defaultMetricName, count);
        MetricsWriterFactory.getWriter().writeCountMetric(defaultMetricName, count);
    }

    public void setMetricName(final String metricName)
    {
        this.defaultMetricName = metricName;
    }

    public String getMetricName()
    {
        return defaultMetricName;
    }

    private void validateCounter()
    {
        validateCounter(defaultMetricName, 1);
    }

    private void validateCounter(final String metricName)
    {
        validateCounter(metricName, 1);
    }


    private static void validateCounter(final String metricName, final long count)
    {
        if (StringUtils.isBlank(metricName))
        {
            throw new IllegalArgumentException("An empty counter name was specified, you must specify a metric name");
        }

        if (count < 0)
        {
            throw new IllegalArgumentException("Counts need to be positive numbers, count: " + count + " is not valid.");
        }
    }

}

