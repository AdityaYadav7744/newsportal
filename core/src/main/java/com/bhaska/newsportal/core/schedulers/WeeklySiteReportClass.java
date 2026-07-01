package com.bhaska.newsportal.core.schedulers;


import org.apache.commons.collections4.map.HashedMap;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Component(
        service = Runnable.class,
        immediate = true
)

@Designate(ocd = WeeklySiteReportConfig.class)
public class WeeklySiteReportClass implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(WeeklySiteReportClass.class);
    private static final String TOPICS = "content/api/job";
    @Reference
    private JobManager jobManager;

    @Reference
    private Scheduler scheduler;

    @Modified
    @Activate
    public void init(WeeklySiteReportConfig config) {

        if (config.state()) {
            LOG.info("=====inside the init method=====");
            ScheduleOptions options = scheduler.EXPR(config.cronExpression());
            scheduler.schedule(this, options);
        } else {
            LOG.info("Status is disable");
        }

    }

    @Override
    public void run() {
        LOG.info("Inside the run method");
        Map<String, Object> map = new HashedMap<>();
        jobManager.addJob(TOPICS, map);

    }
}
