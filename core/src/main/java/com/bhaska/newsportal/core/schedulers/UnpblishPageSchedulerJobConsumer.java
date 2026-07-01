package com.bhaska.newsportal.core.schedulers;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS+"=page/unpublish"
        }
)
public class UnpblishPageSchedulerJobConsumer implements JobConsumer {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private Replicator replicator;

    @Override
    public JobResult process(Job job) {
        Map<String , Object> map = new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE,"newsportal-subservice");
        try( ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(map)){
             Resource resource = resolver.getResource(job.getProperty("path", String.class));
             if(resource==null){
                 return JobResult.FAILED;
             }
            final Iterator<Resource> pages =
                    resolver.findResources("SELECT * FROM [cq:Page] AS s WHERE ISDESCENDANTNODE([/content/newsportal])","JCR-SQL2");
            while(pages.hasNext()){
                 Resource next = pages.next();
                final ValueMap vm = next.getValueMap();
                boolean isExpired = vm.get("isExpired", false);
                if(isExpired){
                    Session session = resolver.adaptTo(Session.class);
                    replicator.replicate(session, ReplicationActionType.DEACTIVATE,next.getPath());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return JobResult.OK;
    }
}
