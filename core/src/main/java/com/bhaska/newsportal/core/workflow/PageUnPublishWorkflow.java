package com.bhaska.newsportal.core.workflow;


import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.commons.ReferenceSearch;

import com.day.cq.wcm.msm.api.LiveRelationship;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.RangeIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(
        service = WorkflowProcess.class,
        property = {
                "process.label=page unpublis workflow"
        }
)
public class PageUnPublishWorkflow implements WorkflowProcess {

    @Reference
    private LiveRelationshipManager liveRelationshipManager;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        String payload = workItem.getWorkflowData().getPayload().toString();
        ResourceResolver resolver = workflowSession.adaptTo(ResourceResolver.class);
        if (resolver == null) {
            throw new WorkflowException("Unable to obtain ResourceResolver.");
        }

        Resource resource = resolver.getResource(payload);

        if (resource == null) {
            throw new WorkflowException("Resource not found: " + payload);
        }

        ReferenceSearch referenceSearch = new ReferenceSearch();
        referenceSearch.setSearchRoot("/content");
        Map<String, ReferenceSearch.Info> search = referenceSearch.search(resolver, payload);
        List<String> reference = new ArrayList<>();
        if (search!=null && !search.isEmpty()){
            for( Map.Entry<String , ReferenceSearch.Info> result: search.entrySet()){
                reference.add(result.getKey());
            }
        }

        /**
         * Finding the MSM Live Copy
         * **/
        List<String> msmLiveCopy = new ArrayList<>();
        try {
            RangeIterator liveRelationships = liveRelationshipManager.getLiveRelationships(resource, null, null);
            while (liveRelationships.hasNext()){
                LiveRelationship relationship =
                        (LiveRelationship)liveRelationships.next();
                msmLiveCopy.add(relationship.getTargetPath());
            }
        } catch (WCMException e) {
            throw new WorkflowException("Unable to fetch MSM Live Copies", e);
        }




        MetaDataMap metadata =
                workItem.getWorkflow().getWorkflowData().getMetaDataMap();

        metadata.put("pagePath", payload);
        metadata.put("referenceCount", reference.size());
        metadata.put("references", String.join(",", reference));
        metadata.put("liveCopyCount", msmLiveCopy.size());
        metadata.put("liveCopies", String.join(",", msmLiveCopy));

    }
}
