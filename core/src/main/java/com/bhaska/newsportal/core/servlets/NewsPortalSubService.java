package com.bhaska.newsportal.core.servlets;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import java.util.HashMap;
import java.util.Map;

@Component(
        service = NewsPortalSubService.class
)
public class NewsPortalSubService {
    @Reference
    ResourceResolverFactory resourceResolverFactory;

    public ResourceResolver getResourceResolver(){
        Map<String , Object> map=new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE, "newsportal-subservice");
        ResourceResolver resourceResolver=null;
        try {
             resourceResolver = resourceResolverFactory.getServiceResourceResolver(map);
             return resourceResolver;
        }catch (Exception e){

        }
        return null;
    }
}
