package com.bhaska.newsportal.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import javax.annotation.PostConstruct;
@Model(adaptables = { SlingHttpServletRequest.class},
defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DealXfReaderMdel {

    @SlingObject
    ResourceResolver resourceResolver;

    @ScriptVariable
    Page currentPage;

    private String image;
    private String id;
    private String longDescription;

    private static final String ROOT_PATH="/content/experience-fragments/newsportal/deals";

    @PostConstruct
    private void init(){
        if (currentPage==null){
            return;
        }

        String name = currentPage.getName();
        final String xfFoldername = "golde".equals(name) ? "golden" : name;
        String xfComponentPath=ROOT_PATH+"/"+xfFoldername+"/master/jcr:content/root/deals_xf_component";
        Resource xfResource = resourceResolver.getResource(xfComponentPath);
        if(xfResource != null){
             ValueMap map = xfResource.getValueMap();
             image = map.get("cardImageReference",String.class);
             longDescription = map.get("longDescription", String.class);
             String path = map.get("selectedDeal", String.class);
             id= path.substring(path.lastIndexOf("/") + 1);
        }
    }
    public String getLongDescription() {
        return longDescription;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }
}