package com.bhaska.newsportal.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CarouselModelV2 {

    @ChildResource(name = "carouselItems")
    private List<CarouselItemV2> carouselItems;

    public List<CarouselItemV2> getCarouselItems() {
        return carouselItems;
    }
}