package com.bhaska.newsportal.core.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import java.util.HashMap;
import java.util.Map;

@Component(
        service = WorkflowProcess.class,
        property = {
                "process.label= Update Asset Meta Data"
        }
)
public class AssetMetaDataUpdate implements WorkflowProcess {

    @Reference
    ResourceResolverFactory resourceResolverFactory;
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        String payload = workItem.getWorkflowData().getPayload().toString();

        Map<String , Object> map= new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE, "newsportal-sitemap");
      try ( ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(map)){
             Resource resource = resourceResolver.getResource(payload);
             ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
             modifiableValueMap.put("jcr:title" , "This title added through code");
             resourceResolver.commit();
        }
        catch (Exception e){

        }

    }
}
