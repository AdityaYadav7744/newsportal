package com.bhaska.newsportal.core.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;

@Component(
        service = WorkflowProcess.class,
        property = {
                "process.label=page delete workflow"
        }
)
public class PageDeleteWorkflow implements WorkflowProcess {
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {

         String payload = workItem.getWorkflowData().getPayload().toString();
         ResourceResolver resolver = workflowSession.adaptTo(ResourceResolver.class);
         if (resolver ==null){
             throw  new WorkflowException("Resolver is null");
         }
         PageManager pageManager = resolver.adaptTo(PageManager.class);

        String pagePath =
                payload.replace(
                        "/jcr:content",
                        "");
         Page page = pageManager.getPage(pagePath);

         if(page==null){
             throw new WorkflowException("Page is null");
         }
        try {
            pageManager.delete(page,false);
        } catch (WCMException e) {
            throw new RuntimeException(e);
        }

    }
}
