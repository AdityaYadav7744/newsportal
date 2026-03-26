package com.bhaska.newsportal.core.listeners;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.Map;

@Component(
        service = ResourceChangeListener.class,
        property ={
                ResourceChangeListener.PATHS+"=/home/users",
                ResourceChangeListener.CHANGES + "=ADDED"
}
)
public class UserCreationListener implements ResourceChangeListener {

    private static final Logger LOG = LoggerFactory.getLogger(UserCreationListener.class);

    @Reference
    JobManager jobManager;



    @Override
    public void onChange(List<ResourceChange> list) {
        LOG.info("In side the OnChangeMethod");
        for (ResourceChange change :list){
            Map<String, Object> map=new HashedMap<>();
            map.put("userPath",change.getPath());
            jobManager.addJob("user/onboarding/job",map);
            LOG.info("Job Created Successfully");
        }
    }
}
