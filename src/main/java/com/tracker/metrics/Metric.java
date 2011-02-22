package com.tracker.metrics;

/**
 * Tracker - a BasicMetricsTest collection and display system
 *
 * User: jtruelove
 * Date: Feb 13, 2011
 * Time: 8:33:52 PM
 */
interface Metric
{
    /**
     * Returns the type of the metric being tracked, i.e. Count or Time
     * @return
     */
    public int getType();
    public long getCount();
    public String getName();
    public void aggregateMetric(Metric metric);
}
