package com.bhaska.newsportal.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CarouselModelV1 {

    @ChildResource(name = "carouselItems")
    private List<CarouselItem> carouselItems;

    public List<CarouselItem> getCarouselItems() {
        return carouselItems;
    }
}