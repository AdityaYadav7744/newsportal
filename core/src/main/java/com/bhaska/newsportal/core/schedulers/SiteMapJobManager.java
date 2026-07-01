package com.bhaska.newsportal.core.schedulers;

import com.bhaska.newsportal.core.config.SiteMapConfiguration;
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
@Designate(ocd = SiteMapConfiguration.class)
public class SiteMapJobManager implements Runnable{

    private static final String JOB_NAME="site-map-job";

    @Reference
    private JobManager jobManager;

    @Reference
    private Scheduler scheduler;

    private String pagePath;

    @Activate
    @Modified
    private void init(SiteMapConfiguration config){
        if(config.status()){
            pagePath = config.pagePath();
            scheduler.unschedule(JOB_NAME);
            ScheduleOptions scheduleOptions = scheduler.EXPR(config.cronExp());
            scheduleOptions.name(JOB_NAME);
            scheduleOptions.canRunConcurrently(false);
            scheduler.schedule(this, scheduleOptions);
        }
    }

    @Override
    public void run() {
        Map<String , Object> map=new HashMap<>();
        map.put("pagePath",pagePath);
        jobManager.addJob("sitempa/scheduler",map);
    }
}
