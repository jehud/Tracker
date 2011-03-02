package com.tracker.metrics;

import org.junit.Test;

import java.io.IOException;

/**
 * Tracker - a BasicMetricsTest collection and display system
 * <p/>
 * User: jtruelove
 * Date: Feb 21, 2011
 * Time: 7:48:54 PM
 */
public class BasicMetricsTest
{
    @Test
    public void testCountOne() throws InterruptedException, IOException
    {
        /*Properties prop = new Properties();
        prop.put("Foo", "bar");
        prop.put("testList", "1,then2,3");
        FileWriter fw = new FileWriter(new File("/Users/jtruelove/JavaProjects/test.txt"));
        prop.store(fw, "test file");*/

        Timer testTimer = new Timer("testTime");
        Counter counter = new Counter("TestCounter");
        counter.countOne();
        counter.countOne("TestOtherCounter");
        Thread.sleep(4000);
        testTimer.stop("midWay");
        counter.count(5);
        counter.count("TestIt", 3);
        testTimer.stop();
        Thread.sleep(10000);
        counter.count(4);
        testTimer.stop("Last");
        for (int i = 0; i < 100; i++)
        {
            testTimer.start();
            counter.count("Test"+i, i);
            Thread.sleep(Math.round(i * 10.25));
            testTimer.stop("TestTimer" + i);
        }
    }
}
