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


@Component(service = Runnable.class,
immediate = true)
@Designate(
        ocd = ScheduledContentExpiry.class
)
public class ScheduledContentJobManagerExpiryClass implements Runnable {

    static final Logger log= LoggerFactory.getLogger(ScheduledContentJobManagerExpiryClass.class);
    private static final String PATH="content/ScheduledContentExpiryClass";

    @Reference
    private JobManager jobManager;

    @Reference
    private Scheduler scheduler;

    @Modified
    @Activate
    public void init(ScheduledContentExpiry sec){
        if(sec.status()){
            log.info("*********** Inside init method **************");
            ScheduleOptions scheduleOptions = scheduler.EXPR(sec.cronExpression());
            scheduler.schedule(this , scheduleOptions);
        }

    }


    @Override
    public void run() {
        log.info("******************* Inside Run method  *****************************");
        Map<String,Object> map=new HashedMap<>();
        jobManager.addJob(PATH,map);

    }
}
