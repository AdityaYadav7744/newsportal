package com.bhaska.newsportal.core.listeners;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(
        service = ResourceChangeListener.class,
        property = {
                ResourceChangeListener.PATHS+"=/content/dam/newsportal",
                ResourceChangeListener.CHANGES+"=ADDED"
        }
)
public class DamMetaDataProcessingJobManagerListner implements ResourceChangeListener {

    @Reference
    JobManager jobManager;
    @Override
    public void onChange(List<ResourceChange> list) {
        for(ResourceChange change : list){
            Map<String , Object> map = new HashMap<>();
            map.put("assetPath",change.getPath());
            map.put("currentTime", LocalDateTime.now().toString());
            map.put("triggeredBy","system");
            jobManager.addJob("dam/metadata/processing",map);
        }

    }
}
