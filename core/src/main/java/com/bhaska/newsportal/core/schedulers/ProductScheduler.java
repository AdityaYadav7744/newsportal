package com.bhaska.newsportal.core.schedulers;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Component(
        service = Runnable.class,
        immediate = true
)

@Designate(ocd = ProductSchedulerConfig.class)

public class ProductScheduler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ProductScheduler.class);

    private String apiUrl;

    @Reference
    private Scheduler scheduler;

    @Reference
    private JobManager jobManager;

    @Activate
    @Modified
    protected void activate(ProductSchedulerConfig config) {

        log.info("ProductScheduler Activate Method Started");

        // Remove old scheduler if already exists
        scheduler.unschedule(config.schedulerName());

        this.apiUrl = config.apiUrl();

        if (config.enable()) {

            ScheduleOptions scheduleOptions = scheduler.EXPR(config.cronExpression());
            scheduleOptions.name(config.schedulerName());
            scheduleOptions.canRunConcurrently(false);

            scheduler.schedule(this, scheduleOptions);

            log.info("Scheduler started with CRON: {}", config.cronExpression());
        } else {
            log.info("Scheduler is disabled from configuration");
        }
    }

    @Override
    public void run() {

        log.info("Inside Scheduler Run Method");

        Map<String, Object> props = new HashMap<>();
        props.put("apiUrl", apiUrl);


        log.info("API URL while creating job: {}", apiUrl);

        jobManager.addJob("newsportal/productScheduler", props);
    }
}