package com.tracker.metrics;

/**
 * Tracker - a BasicMetricsTest collection and display system
 *
 * User: jtruelove
 * Date: Feb 13, 2011
 * Time: 9:14:16 PM
 */
class CountMetric implements Metric
{
    private long count;
    private String name;
    private int observations;
    private long timeStamp;

    CountMetric(String name, long count)
    {
       this.name = name;
       this.count = count;
       observations = 1;
       timeStamp = System.currentTimeMillis();
    }

    public int getType()
    {
        return 1;
    }

    public long getCount()
    {
        return Math.round((double)count / (double)observations);
    }

    public String getName()
    {
        return name;
    }

    public void aggregateMetric(Metric metric)
    {
        count += metric.getCount();
        observations++;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getType());
        builder.append(",");
        builder.append(name);
        builder.append(",");
        builder.append(getCount());
        builder.append(",");
        builder.append(System.currentTimeMillis());
        return builder.toString();
    }
}
