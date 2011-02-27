package com.tracker.metrics;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handles writing BasicMetricsTest to disk in a synchronized effecient manner
 * <p/>
 * User: jtruelove
 * Date: Feb 13, 2011
 * Time: 5:09:19 PM
 */
class MetricsWriterFactory
{
    /**
     * Default logger
     */
    private static Logger logger = LoggerFactory.getLogger(MetricsWriterFactory.class);

    /**
     * Handles lazy init of writer in a thread safe way
     */
    private static class WriteHandler
    {
        public static MetricWriter writer = new MetricWriter();
    }

    protected static MetricWriter getWriter()
    {
        return WriteHandler.writer;
    }

    static class MetricWriter
    {

        /**
         * Basic init constructor
         */
        private MetricWriter()
        {
            HashMap<String, String> foo;
            queue = new LinkedBlockingQueue<Metric>();
            groupedMetrics = new ConcurrentHashMap<String, Metric>();
            Runtime.getRuntime().addShutdownHook(new ShutdownHook());

            // default to 10 seconds, this will be config driven
            diskWritePeriodInMilli = 10000;
            queueDrainPeriodInMilli = diskWritePeriodInMilli / 2;
            timingHandler = new Timer();
            timingHandler.scheduleAtFixedRate(new QueueDrainer(), queueDrainPeriodInMilli, queueDrainPeriodInMilli);
            timingHandler.scheduleAtFixedRate(new DiskWriter(), diskWritePeriodInMilli, diskWritePeriodInMilli);
            logger.info("Initialized BasicMetricsTest writer {}", this);
        }


        /**
         * How often do we write the metrics to disk
         */
        private long diskWritePeriodInMilli;

        /**
         * How often do we flush the queue of data to the aggregated map
         */
        private long queueDrainPeriodInMilli;

        /**
         * Queue to hold pending BasicMetricsTest
         */
        private LinkedBlockingQueue<Metric> queue;

        /**
         * Holds all the metrics for a period
         */
        private ConcurrentHashMap<String, Metric> groupedMetrics;

        /**
         * The timer to manage various scheduled work
         */
        private Timer timingHandler;

        /**
         * Handles storing a count metric before it's written to disk
         *
         * @param name  the name of the metric being counted
         * @param count the count of the metric passed in
         */
        void writeCountMetric(final String name, final long count)
        {
            queue.add(new CountMetric(name, count));

        }

        /**
         * Handles storing a timer metric before it's written to disk
         *
         * @param name        the name of the timer metric
         * @param timeInMilli the time taken in milliseconds
         */
        void writeTimerMetric(final String name, final double timeInMilli)
        {
            queue.add(new TimeMetric(name, timeInMilli));
        }


        /**
         * Handles periodically moving items in the queue to the aggregated map of BasicMetricsTest.
         */
        private void drainQueue()
        {
            List<Metric> list = null;
            // block and drain
            list = new ArrayList<Metric>(queue.size());
            queue.drainTo(list);
            logger.debug("Flushed {} items from queue", list.size());
            // aggregate the BasicMetricsTest
            for (Metric metric : list)
            {
                // just update in place if a metric exists
                if (groupedMetrics.containsKey(metric.getName()))
                {
                    groupedMetrics.get(metric.getName()).aggregateMetric(metric);
                }
                else
                {
                    //create one if none exists
                    groupedMetrics.put(metric.getName(), metric);
                }
            }
        }

        /**
         * Writes the metrics to disk
         */
        private void writeStatisticsToDisk()
        {
            drainQueue();
            synchronized (groupedMetrics)
            {
                if (groupedMetrics.size() > 0)
                {
                    StringBuilder builder = new StringBuilder();
                    //start the line with the timestamp of the writing
                    builder.append(System.currentTimeMillis()).append(";");
                    for (Metric metric : groupedMetrics.values())
                    {
                        builder.append(metric);
                        builder.append(";");
                    }

                    String metrics = builder.toString();
                    if (!StringUtils.isBlank(metrics))
                    {
                        // TODO figure out long term which / how to configure this logger in the best way
                        logger.info(StringUtils.chop(metrics));
                        groupedMetrics.clear();
                    }
                }
            }

        }

        /**
         * Basic toString function
         *
         * @return
         */
        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append("<MetricsWriter writeFrequency=");
            builder.append(diskWritePeriodInMilli);
            builder.append("(ms), queueDrainFrequency=");
            builder.append(queueDrainPeriodInMilli);
            builder.append("(ms)>");
            return builder.toString();
        }

        /**
         * Timer thread to move the queue to the aggregated BasicMetricsTest map
         */
        private class QueueDrainer extends TimerTask
        {
            public void run()
            {
                try
                {
                    drainQueue();
                }
                catch (Exception ex)
                {
                    logger.error("Got exception while attempting to drain internal metric queue, ex: " + ex);
                }
            }
        }

        /**
         * Timer thread to write the stats to disk periodically
         */
        private class DiskWriter extends TimerTask
        {
            public void run()
            {
                try
                {
                    writeStatisticsToDisk();
                }
                catch (Exception ex)
                {
                    logger.error("Got exception while attempting to write BasicMetricsTest to disk, ex: " + ex);
                }
            }
        }

        /**
         * Handles flushing whatever in memory to disk on a shutdown event
         */
        private class ShutdownHook extends Thread
        {
            @Override
            public void start()
            {
                try
                {
                    writeStatisticsToDisk();
                }
                catch (Exception ex)
                {
                    logger.error("Got exception while attempting to flush BasicMetricsTest on shutdown, ex: " + ex);
                }
            }
        }
    }
}
