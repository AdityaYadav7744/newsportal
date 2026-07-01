package com.bhaska.newsportal.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Model(adaptables = HttpServletRequest.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class MigrationModel {
    @ScriptVariable
    private Page currentPage;

    private String title;
    private String description;
    private String bodyHtml;
    private String image;


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public String getImage() {
        return image;
    }

    @PostConstruct
    private void init(){

        if(currentPage!=null){
            title = currentPage.getProperties().get("jcr:title", String.class);
           description = currentPage.getProperties().get("jcr:description",String.class);
            bodyHtml= currentPage.getProperties().get("bodyHtml",String.class);
            image = currentPage.getProperties().get("image",String.class);
        }
    }
}
