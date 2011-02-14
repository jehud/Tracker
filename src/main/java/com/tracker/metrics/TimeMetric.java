package com.tracker.metrics;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracker - a metrics collection and display system
 * 
 * User: jtruelove
 * Date: Feb 13, 2011
 * Time: 9:14:35 PM
 */
class TimeMetric implements Metric
{
    private List<Long> times;
    private long count;
    private String name;
    private int observations;

    /**
     * not really used now maybe down the road
     */
    private long timeStamp;

    TimeMetric(String name, long time)
    {
       times = new ArrayList<Long>();
       this.name = name;
       count = 1;
       observations = 1;
       timeStamp = System.currentTimeMillis();
       times.add(time);
    }

    public int getType()
    {
        return 0;
    }

    public long getCount()
    {
        return count;
    }

    public int getNumberOfObservations()
    {
        return observations;
    }

    public String getName()
    {
        return name;
    }

    public long getTimeStamp()
    {
        return timeStamp;
    }

    public long getTime()
    {
        if(times.size() < 1)
        {
            throw new IllegalArgumentException("Times should never be 0, this class is internal");
        }

        long total = 0;
        for (long time : times)
        {
           total += time;
        }
        return total / times.size();
    }

    public void aggregateMetric(Metric metric)
    {
        if (metric instanceof TimeMetric)
        {
            count++;
            observations++;
            times.add(((TimeMetric)metric).getTime());
        }
        else
        {
            // throw exception
        }
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getType());
        builder.append(",");
        builder.append(name);
        builder.append(",");
        builder.append(getTime());
        builder.append(",");
        builder.append(count);
        builder.append(",");
        builder.append(System.currentTimeMillis());
        return builder.toString();
    }
}