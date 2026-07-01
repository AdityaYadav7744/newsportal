package com.bhaska.newsportal.core.models;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.api.resource.Resource;
import javax.annotation.PostConstruct;



@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CustomCFModel {


    @SlingObject
    private Resource resource;
    
    private String title;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    private String description;

    @PostConstruct
    private void init(){
        ContentFragment contentFragment = resource.adaptTo(ContentFragment.class);
        ContentElement content = contentFragment.getElement("title");
        title = content.getContent();
        ContentElement desc = contentFragment.getElement("textArea");
        description=desc.getContent();
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
