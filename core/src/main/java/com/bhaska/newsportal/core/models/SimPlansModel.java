package com.bhaska.newsportal.core.models;


import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class SimPlansModel {

    @ValueMapValue
    private String pagePath;
    @ValueMapValue
    private String damPath;

    @SlingObject
    private ResourceResolver resourceResolver;

    private List<SimPlans> plans;

    @PostConstruct
    private void init() {
        plans = new ArrayList<>();
        Resource resource = resourceResolver.getResource(damPath);
        if (resource != null) {
            for (Resource child : resource.getChildren()) {
                ContentFragment contentFragment = child.adaptTo(ContentFragment.class);
                if (contentFragment == null) {
                    continue;
                }
                String planName = getElementValue(contentFragment, "planName");
                String uuid = getElementValue(contentFragment, "uuid");
                String price = getElementValue(contentFragment, "price");
                String validityDays = getElementValue(contentFragment, "validityDays");
                String planDescription = getElementValue(contentFragment, "planDescription");

                plans.add(new SimPlans(planName, uuid, price, validityDays, planDescription));
            }
        }
    }


    public String getPagePath() {
        return pagePath;
    }

    public List<SimPlans> getPlans() {
        return plans;
    }
    public String getDamPath() {
        return damPath;
    }

    private String getElementValue(ContentFragment contentFragment , String elementName){
         ContentElement element = contentFragment.getElement(elementName);
         if(element!=null){
             return element.getContent();
         }
        return "";
    }
}
