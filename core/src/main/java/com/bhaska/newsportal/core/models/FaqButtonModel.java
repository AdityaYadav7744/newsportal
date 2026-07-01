package com.bhaska.newsportal.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Model(
        adaptables = HttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FaqButtonModel {

    @ScriptVariable
    private Page currentPage;
    String path;
    public String getPath() {
        return path;
    }


    @PostConstruct
    public void init(){
         Page parent = currentPage.getParent();
          this.path = parent.getPath();
         if(path!=null){
             this.path+=".html";
         }
         else{
             this.path="#";
         }
    }
}
