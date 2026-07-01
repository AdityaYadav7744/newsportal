package com.bhaska.newsportal.core.schedulers;

import com.bhaska.newsportal.core.config.UnpblishPageSchedulerConfig;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import java.util.HashMap;
import java.util.Map;

@Component(
        service = Runnable.class,
        immediate = true
)
@Designate(ocd =  UnpblishPageSchedulerConfig.class)
public class UnpblishPageSchedulerJobManager implements Runnable {

    private static final String JOB_NAME="unpublishpages";
    @Reference
    private JobManager jobManager;

    @Reference
    private Scheduler scheduler;

    private String path;

    @Activate
    @Modified
    public void inti(UnpblishPageSchedulerConfig config){
        if(config.status()){
            this.path = config.path();
            scheduler.unschedule(JOB_NAME);
            ScheduleOptions scheduleOptions = scheduler.EXPR(config.cron());
            scheduleOptions.name(JOB_NAME);
            scheduler.schedule(this, scheduleOptions);
        }
    }

    @Override
    public void run() {
        Map<String, Object> map =new HashMap<>();
        map.put("path",path);
        jobManager.addJob("page/unpublish",map);
    }
}
