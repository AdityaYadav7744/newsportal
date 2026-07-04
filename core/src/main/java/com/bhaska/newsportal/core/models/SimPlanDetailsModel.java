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
import javax.servlet.http.HttpServletRequest;


@Model(adaptables = { HttpServletRequest.class, Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class SimPlanDetailsModel {

    @SlingObject
    private HttpServletRequest request;

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    private String damPath;

    private String planName;
    private String uuid;
    private String price;
    private String validityDays;
    private String planDescription;

    @PostConstruct
    private void init(){

        String parameter = request.getParameter("planId");
        if(damPath==null || parameter==null){
            return;
        }

        Resource resource = resourceResolver.getResource(damPath);
        if (resource == null){
            return;
        }

        for(Resource res : resource.getChildren()){
            ContentFragment cf = res.adaptTo(ContentFragment.class);
            if(cf==null){
                continue;
            }
            String uuid1 = getElementValue(cf, "uuid");
            if(parameter.equals(uuid1)){
                this.uuid = uuid1;
                this.planName = getElementValue(cf, "planName");
                this.price = getElementValue(cf, "price");
                this.validityDays = getElementValue(cf, "validityDays");
                this.planDescription = getElementValue(cf, "planDescription");
                break;
            }
        }
    }

    private String getElementValue(ContentFragment cf , String name){
         ContentElement element = cf.getElement(name);
        return element !=null ? element.getContent() : "" ;
    }



    public String getDamPath() {
        return damPath;
    }

    public String getPlanName() {
        return planName;
    }

    public String getPrice() {
        return price;
    }

    public String getUuid() {
        return uuid;
    }

    public String getValidityDays() {
        return validityDays;
    }

    public String getPlanDescription() {
        return planDescription;
    }

}
