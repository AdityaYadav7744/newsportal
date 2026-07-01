package com.bhaska.newsportal.core.service;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import java.util.Map;

@Component(service = NPUtilService.class)
public class NPUtilService {

    @Reference
    ResourceResolverFactory factory;

    public ResourceResolver getResourceResolver()  {
        Map<String , Object> map= new HashedMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE,"newsportal-subservice");
        ResourceResolver res=null;
        try {
            res= factory.getServiceResourceResolver(map);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }

        return  res;
    }
}
