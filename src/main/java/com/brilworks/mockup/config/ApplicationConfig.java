package com.brilworks.mockup.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;
import java.util.UUID;

@Configuration
public class ApplicationConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);

    private final String id;
    private final long startTime;

    public ApplicationConfig() {
        this.id = UUID.randomUUID().toString();
        LOG.info("APPID: {}", id);

        this.startTime = System.currentTimeMillis();
        LOG.info("START: {}", startTime);

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        /* Total number of processors or cores available to the JVM */
        LOG.info("CPU Cores: {}", Runtime.getRuntime().availableProcessors());

        /* Total amount of free memory available to the JVM */
        String freeMemory = (int) (Runtime.getRuntime().freeMemory() / 1000000) + "Mb";
        LOG.info("Memory Free: {}", freeMemory);

        /* Total memory currently in use by the JVM */
        String memoryInUse = (int) (Runtime.getRuntime().totalMemory() / 1000000) + "Mb";
        LOG.info("Memory In Use: {}", memoryInUse);

        /* This will return Long.MAX_VALUE if there is no preset limit */
        long maxMemory = Runtime.getRuntime().maxMemory();
        /* Maximum amount of memory the JVM will attempt to use */
        String maxMemoryMb = maxMemory == Long.MAX_VALUE ? "no limit" : (int) (maxMemory / 1000000) + "Mb";
        LOG.info("Memory Maximum: {}", maxMemoryMb);

    }

    public String getId() {
        return id;
    }

    public long getStartTime() {
        return startTime;
    }
}
