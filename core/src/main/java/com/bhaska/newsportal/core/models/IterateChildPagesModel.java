package com.bhaska.newsportal.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = HttpServletRequest.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "/content/us/en/home"
)
public class IterateChildPagesModel {

    @SlingObject
    private Page page;

    private List<String> listOfPages = new ArrayList<>();

    @PostConstruct
    private void init(){
        Iterator<Page>  liatOfPages = page.listChildren();
        while (liatOfPages.hasNext()){
             Page next = liatOfPages.next();
             ValueMap properties = next.getProperties();
            listOfPages.add(properties.get("jcr:title",String.class));
            listOfPages.add(properties.get("path",String.class));
        }
    }
    public List<String> getListOfPages() {
        return listOfPages;
    }

}
