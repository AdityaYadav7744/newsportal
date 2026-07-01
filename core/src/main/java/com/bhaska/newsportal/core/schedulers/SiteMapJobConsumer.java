package com.bhaska.newsportal.core.schedulers;

import com.bhaska.newsportal.core.servlets.NewsPortalSubService;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        service = JobConsumer.class,
        property = {
            JobConsumer.PROPERTY_TOPICS+"=sitempa/scheduler"
        }
)
public class SiteMapJobConsumer implements JobConsumer {

    @Reference
    private  NewsPortalSubService service;

    @Override
    public JobResult process(Job job) {
        String pagePath = job.getProperty("pagePath", String.class);
        try (ResourceResolver resourceResolver = service.getResourceResolver()){

        }
        catch (Exception e){

        }
        return null;
    }
}
