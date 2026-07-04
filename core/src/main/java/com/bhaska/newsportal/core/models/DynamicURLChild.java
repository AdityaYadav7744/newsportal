package com.bhaska.newsportal.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class DynamicURLChild {

    @SlingObject
    private SlingHttpServletRequest request;

    private String selector;
    private String url;
    private String suffix;
    private String queryParam;

    public String getQueryParam() {
        return queryParam;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getSelector() {
        return selector;
    }

    public String getUrl() {
        return url;
    }

    @PostConstruct
    private void init(){

        // Getting Selector
        String[] arr = request.getRequestPathInfo().getSelectors();
          this.url = request.getRequestURL().toString();
        if (arr.length>0){
             this.selector = arr[0];
        }

        //Getting suffix
        String suff = request.getRequestPathInfo().getSuffix();
        if(suff!=null){
            this.suffix = suff.substring(1);
        }

        //getting Query param
        String headphone = request.getParameter("headphone");
        if(headphone !=null || headphone.isEmpty()){
            this.queryParam=headphone;
        }
        else {
            this.queryParam="QueryParam is empty";
        }
    }
}
