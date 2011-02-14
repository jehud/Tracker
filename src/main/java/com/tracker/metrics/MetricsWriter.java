package com.tracker.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: jtruelove
 * Date: Feb 13, 2011
 * Time: 5:09:19 PM
 * To change this template use File | Settings | File Templates.
 */
class MetricsWriter
{
    private static LinkedBlockingQueue<Metric> queue;

    private static ConcurrentHashMap<String, Metric> groupedMetrics;

    private static Timer queueDrainer;

    private static Timer diskWriter;

    static
    {
        queue = new LinkedBlockingQueue<Metric>();
        groupedMetrics = new ConcurrentHashMap<String, Metric>();

    }

    protected static void writeCountMetric(String name, long count)
    {
        long currentTime = System.nanoTime();

    }

    protected static void writeTimerMetric(String name, long timeInMilli)
    {
        long currentTime = System.nanoTime();
    }


    private static void drainQueue()
    {
        List<Metric> list;
        synchronized (queue)
        {
            list = new ArrayList<Metric>(queue.size());
            queue.drainTo(list);
        }

        for (Metric metric : list)
        {
            if (groupedMetrics.containsKey(metric.getName()))
            {
                groupedMetrics.get(metric.getName()).aggregateMetric(metric);
            }
            else
            {
                groupedMetrics.put(metric.getName(), metric);
            }
        }
    }

    private static void writeStatisticsToDisk()
    {
        drainQueue();
        synchronized (groupedMetrics)
        {
            StringBuilder builder = new StringBuilder();
            for (Metric metric : groupedMetrics.values())
            {
                builder.append(metric);
                builder.append(";");
            }
            // TODO remove last char
            String metricString = builder.toString();
            // TODO add write to disk mechanism
            groupedMetrics.clear();
        }

    }

    class QueueDrainer extends TimerTask
    {
        public void run()
        {
            drainQueue();
        }
    }

    class DiskWriter extends TimerTask
    {
        public void run()
        {
            writeStatisticsToDisk();
        }
    }
}
