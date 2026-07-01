package com.bhaska.newsportal.core.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.service.component.annotations.Component;

import java.util.HashMap;
import java.util.Map;

@Component(
        service = WorkflowProcess.class,
        property = {
                "process.label=MetaData Update Asset"
        }
)
public class MetaDataUpdateAsset implements WorkflowProcess {

    @OSGiService
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {

        String path = workItem.getWorkflow().getWorkflowData().getPayload().toString();
        Map<String, Object> map=new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE,"subservice name");
        try(ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(map);){
             Resource resource = resourceResolver.getResource(path);
             ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
             modifiableValueMap.put("author","admin");
             resourceResolver.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
