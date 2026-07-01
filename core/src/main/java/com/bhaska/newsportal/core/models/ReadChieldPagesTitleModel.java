package com.bhaska.newsportal.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ReadChieldPagesTitleModel {

    @SlingObject
    private ResourceResolver resourceResolver;

    private List<String> list;

    @PostConstruct
    private void init(){
        list=new ArrayList<>();
        Resource resource = resourceResolver.getResource("/content/newsportal/us/en");
         Page page = resource.adaptTo(Page.class);
         Iterator<Page> iterator = page.listChildren();
         while (iterator.hasNext()){
              Page chieldPage = iterator.next();
              list.add(chieldPage.getTitle());
         }
    }
     public List<String> getList(){
        return list;
    }

}
