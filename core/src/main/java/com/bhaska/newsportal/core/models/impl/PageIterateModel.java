package com.bhaska.newsportal.core.models.impl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;

@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PageIterateModel {

    private String pagPath="/content/fruitables";

    @SlingObject
    private ResourceResolver resourceResolver;

    private List<String> list;

    @PostConstruct
    private void inti(){
        Resource resource = resourceResolver.getResource(pagPath);
        Iterator<Resource> resourceIterator = resource.listChildren();

    }

}
