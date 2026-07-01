package com.bhaska.newsportal.core.models;

import com.bhaska.newsportal.core.config.CAConfig;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Model(
        adaptables = HttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CustomCAConfigModel {

    private static final Logger LOG= LoggerFactory.getLogger(CustomCAConfigModel.class);

    private String language;
    private String siteName;


    @ScriptVariable
    private Page currentPage;

    @SlingObject
    private ResourceResolver resourceResolver;

    public String getLanguage() {
        return language;
    }

    public String getSiteName() {
        return siteName;
    }

    @PostConstruct
    private void init(){
        LOG.info("CustomConfigModel is executing inside init method");

        CAConfig config = getConfig(currentPage.getPath(), resourceResolver);
        LOG.info("This is the configuration {}",config);
        if (config!=null){
            this.siteName= config.siteName();
            this.language= config.language();
        }

    }

    private CAConfig getConfig(String path, ResourceResolver resourceResolver){
        Resource resource = resourceResolver.getResource(path);
        LOG.info("inside the getConfig method Resource Object {}", resource);

        if (resource == null) {
            return null;
        }

        ConfigurationBuilder builder = resource.adaptTo(ConfigurationBuilder.class);
        if(builder != null){
            return builder.as(CAConfig.class);
        }
        return null;
    }
}
