package com.tracker.metrics;

/**
 * Tracker - a metrics collection and display system
 *
 * User: jtruelove
 * Date: Feb 13, 2011
 * Time: 8:33:52 PM
 */
interface Metric
{
    public int getType();
    public long getCount();
    public int getNumberOfObservations();
    public String getName();
    public long getTimeStamp();
    public void aggregateMetric(Metric metric);
}
