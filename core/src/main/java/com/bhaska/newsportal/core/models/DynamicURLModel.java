package com.bhaska.newsportal.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { Resource.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class DynamicURLModel {

    @ValueMapValue
    private String pagePath;

    public String getSuffix() {
        return suffix;
    }

    private String selector="phone";
    private String suffix="laptop";
    private String queryParam="headphone";

    public String getSelector() {
        return selector;
    }

    public String getQueryParam() {
        return queryParam;
    }

    public String getPagePath() {
        return pagePath;
    }
}
