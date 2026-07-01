package com.bhaska.newsportal.core.listeners;

import com.day.cq.dam.api.Asset;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.Map;

@Component(
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS+"=dam/metadata/processing"
        }
)
public class DamMetaDataProcessingJobConsumer implements JobConsumer {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private SlingSettingsService slingSettingsService;

    @Override
    public JobResult process(Job job) {
         String assetPath = job.getProperty("assetPath", String.class);
         String currentTime = job.getProperty("currentTime", String.class);
         String triggeredBy = job.getProperty("triggeredBy", String.class);
        Map<String,Object> map=new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE,"newsportal-subservice");
        try( ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(map)){
            if(slingSettingsService.getRunModes().contains("publish")){
                return JobResult.FAILED;
            }

            Resource resource = resourceResolver.getResource(assetPath);
            Asset asset = resource.adaptTo(Asset.class);
            Resource reportRoot = resourceResolver.getResource("/var/report");
            if(reportRoot==null){
                Resource varResource = resourceResolver.getResource("/var");
                Map<String, Object> reportMap = new HashMap<>();
                reportMap.put("report","nt:unstructured");
                resourceResolver.create(varResource,"report",map);
            }
            Map<String, Object> props=new HashMap<>();
            props.put("jcr:primaryType","nt:unstructured");
            props.put("assetPath",assetPath);
            props.put("time",currentTime);
            props.put("triggiredBy",triggeredBy);
            resourceResolver.create(reportRoot, "assetReport",props );
            resourceResolver.commit();
        } catch (Exception e ){
            e.printStackTrace();
        }
        return JobResult.OK;
    }
}
