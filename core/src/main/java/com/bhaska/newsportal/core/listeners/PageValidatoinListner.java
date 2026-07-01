package com.bhaska.newsportal.core.listeners;

import org.apache.sling.api.resource.*;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(
        service = ResourceChangeListener.class,
        property = {
                ResourceChangeListener.PATHS+"=/content/newsportal",
                ResourceChangeListener.CHANGES+"=ADDED",
                ResourceChangeListener.CHANGES+"=CHANGED"
        },
        immediate = true
)
public class PageValidatoinListner implements ResourceChangeListener {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public void onChange(List<ResourceChange> list) {
        Map<String, Object> map= new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE,"newsportal-subservice");
        try (ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(map)){

        } catch (Exception e){

        }
    }
}
