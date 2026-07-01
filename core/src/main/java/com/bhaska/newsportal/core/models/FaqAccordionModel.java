package com.bhaska.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import java.util.List;

@Model(adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FaqAccordionModel {

    @ChildResource
    private List<FaqChildAccordioinModel> accordionItems;

    public List<FaqChildAccordioinModel> getAccordionItems() {
        return accordionItems;
    }
}
