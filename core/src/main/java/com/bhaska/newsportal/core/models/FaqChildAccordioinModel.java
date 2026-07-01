package com.bhaska.newsportal.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FaqChildAccordioinModel {

    @ValueMapValue
    private String title;
    @ValueMapValue
    private String image;
    @ValueMapValue
    private String description;

    public String getDescription() {
        return description;
    }
    public String getImage() {
        return image;
    }
    public String getTitle() {
        return title;
    }

}
