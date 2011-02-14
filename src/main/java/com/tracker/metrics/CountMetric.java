package com.tracker.metrics;

/**
 * Tracker - a metrics collection and display system
 * <p/>
 * User: jtruelove
 * Date: Feb 13, 2011
 * Time: 9:14:16 PM
 */
class CountMetric implements Metric
{
    public int getType()
    {
        return 1;
    }

    public long getCount()
    {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getNumberOfObservations()
    {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getName()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public long getTimeStamp()
    {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void aggregateMetric(Metric metric)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
