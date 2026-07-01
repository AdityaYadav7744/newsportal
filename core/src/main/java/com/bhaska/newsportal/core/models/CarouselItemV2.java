package com.bhaska.newsportal.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CarouselItemV2 {

    @ValueMapValue
    private String mediaType;

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String videoPath;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String buttonText;

    @ValueMapValue
    private String buttonLink;

    public String getMediaType() {
        return mediaType;
    }

    public String getImage() {
        return image;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getTitle() {
        return title;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getButtonLink() {
        return buttonLink;
    }
}