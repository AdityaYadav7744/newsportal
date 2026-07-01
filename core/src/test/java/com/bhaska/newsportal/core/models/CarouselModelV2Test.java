package com.bhaska.newsportal.core.models;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class CarouselModelV2Test {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {

        context.addModelsForClasses(
                CarouselModelV2.class,
                CarouselItemV2.class
        );

        context.load().json("/carouselmodelv2.json", "/content");
    }


    @Test
    void testCarouselItemsMissing() {
        Resource resource = context.resourceResolver()
                .getResource("/content/emptyCarousel");
        CarouselModelV2 model = resource.adaptTo(CarouselModelV2.class);
        assertNotNull(model);
        assertNull(model.getCarouselItems());
    }
}