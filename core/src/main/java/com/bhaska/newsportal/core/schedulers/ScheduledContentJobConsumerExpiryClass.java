package com.bhaska.newsportal.core.schedulers;

import com.bhaska.newsportal.core.service.NPUtilService;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import com.day.cq.search.*;

import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component(
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=content/ScheduledContentExpiryClass"
        }
)
public class ScheduledContentJobConsumerExpiryClass implements JobConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(ScheduledContentJobConsumerExpiryClass.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private Replicator replicator;

    @Reference
    NPUtilService npUtilService;

    @Override
    public JobResult process(Job job) {

        log.info("===== JOB CONSUMER STARTED =====");

        ResourceResolver resolver = null;

        try {


            resolver = npUtilService.getResourceResolver();
            log.info("Resolver obtained");

            Session session = resolver.adaptTo(Session.class);

            if (session == null) {
                log.error("Session is NULL");
                return JobResult.FAILED;
            }

            String currentTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                    .format(new Date());


            Map<String, String> queryMap = new HashMap<>();

            queryMap.put("path", "/content/newsportal/us/en/test");
            queryMap.put("type", "cq:PageContent");


            queryMap.put("property", "cq:expiryDate");
            queryMap.put("property.operation", "exists");


            queryMap.put("daterange.property", "cq:expiryDate");
            queryMap.put("daterange.upperBound", String.valueOf(System.currentTimeMillis()));

            queryMap.put("p.limit", "-1");

            Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
            SearchResult result = query.getResult();

            log.info("Total expired pages: {}", result.getHits().size());


            for (Hit hit : result.getHits()) {

                String pagePath = hit.getPath().replace("/jcr:content", "");

                log.info("Unpublishing: {}", pagePath);

                try {
                    replicator.replicate(session, ReplicationActionType.DEACTIVATE, pagePath);
                    log.info("Unpublishing Success====:   {}", pagePath);
                } catch (Exception e) {
                    log.error("Replication failed: {}", pagePath, e);
                }
            }

            log.info("===== JOB COMPLETED =====");
            return JobResult.OK;

        } catch (Exception e) {
            log.error("Error in Job Consumer", e);
            return JobResult.FAILED;

        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }
    }
}