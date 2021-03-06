package com.tracker.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Tracker - a BasicMetricsTest collection and display system
 *
 * User: jtruelove
 * Date: Feb 13, 2011
 * Time: 7:15:31 PM
 */
public class ConfigManager
{
    /**
     * Default logger
     */
    private static Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    /**
     * Constants used to reference property file and properties
     */
    private static final String CONFIG_FILE = "/tracker.config";
    private static final String WRITE_FREQUENCY_KEY = "write.frequency.in.seconds";
    private static final String APPENDER_NAME_KEY = "metric.appender.name";

    /**
     * Default values if no properties file
     */
    private static final String WRITE_FREQUENCY_DEFAULT = "10";
    private static final String APPENDER_NAME_DEFAULT = "metrics-appender";

    /**
     * Properties set by user
     *
     * TODO consider long term if we watch this file and update these settings dynamically without a restart, listeners etc..
     */
    private static String appenderName;
    private static int writeFrequency;

    /**
     * Holds the state of if configuration was loaded correctly
     */
    private static boolean initialized = true;

    /**
     * Properties that must be set for the metrics library, add more as needed
     */
    private static String[] requiredProperties = {WRITE_FREQUENCY_KEY, APPENDER_NAME_KEY};

    private ConfigManager()
    {}

    /**
     * Tracker static constructor
     */
    static
    {
        Properties properties = null;
        try
        {
            properties = loadProperties();
            validateProperties(properties);

            appenderName = properties.getProperty(APPENDER_NAME_KEY);
            int tmpInt = -1;
            try
            {
                tmpInt = Integer.parseInt(properties.getProperty(WRITE_FREQUENCY_KEY));
            }
            catch (NumberFormatException ex)
            {
                logger.error("Invalid value specified for property " + WRITE_FREQUENCY_KEY + " of " +
                        properties.getProperty(WRITE_FREQUENCY_KEY) + ", this must be set to an integer.");
                initialized = false;
            }
            writeFrequency = tmpInt;
        }
        catch (IOException e)
        {
            initialized = false;
            e.printStackTrace();
        }
    }

    public static String getAppenderName()
    {
        return appenderName;
    }

    public static int getWriteFrequency()
    {
        return writeFrequency;
    }

    public static boolean isInitialized()
    {
        return initialized;
    }

    private static Properties loadProperties() throws IOException
    {

        FileReader reader = null;
        Properties properties = new Properties();
        try
        {
            URL configFile = ConfigManager.class.getResource(CONFIG_FILE);
            if(null == configFile || StringUtils.isBlank(configFile.getFile()))
            {
                properties.setProperty(WRITE_FREQUENCY_KEY, WRITE_FREQUENCY_DEFAULT);
                properties.setProperty(APPENDER_NAME_KEY, APPENDER_NAME_DEFAULT);
                logger.warn("Tracker config file " + CONFIG_FILE + " not found in classpath using defaults.");
            }
            else
            {
                logger.info("Using tracker.config file located at " + configFile);
                reader = new FileReader(configFile.getFile());
                properties.load(reader);
                reader.close();
            }
        }
        catch (IOException ex)
        {
            // TODO should we just go to standard error here?
            logger.error("Failed to load properties from " + CONFIG_FILE + " got exception: " + ex);
            throw ex;
        }
        return properties;
    }

    /**
     * Validated the expected properties
     *
     * @param properties
     */
    private static void validateProperties(Properties properties)
    {
        for (String key : requiredProperties)
        {
            if (!properties.containsKey(key))
            {
                throw new IllegalStateException("Required property " + key + " was not specified. All required properties must be specified");
            }
        }
    }
}
