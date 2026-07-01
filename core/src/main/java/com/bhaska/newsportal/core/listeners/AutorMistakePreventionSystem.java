package com.bhaska.newsportal.core.listeners;

import com.bhaska.newsportal.core.servlets.NewsPortalSubService;
import com.day.cq.replication.Preprocessor;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        service = Preprocessor.class
)
public class AutorMistakePreventionSystem implements Preprocessor {

    @Reference
    private NewsPortalSubService subService;

    @Override
    public void preprocess(ReplicationAction replicationAction, ReplicationOptions replicationOptions) throws ReplicationException {
         String path = replicationAction.getPath();
         try ( ResourceResolver resolver = subService.getResourceResolver()){
         Resource resource= resolver.getResource(path);
             ValueMap valueMap= resource.adaptTo(ValueMap.class);
             String title = valueMap.get("jcr:title", String.class);
              if(title==null){
                throw new  ReplicationException("For publishing the page page title is mandatory ");
              }

         }
         catch (Exception e){

         }

    }
}
