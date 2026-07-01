package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class CarouselModelV1Test {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {

        context.addModelsForClasses(
                CarouselModelV1.class,
                CarouselItem.class
        );

        context.load().json("/carouselmodel.json", "/content");
    }

    @Test
    void testCarouselItemsPresent() {

        Resource resource =
                context.resourceResolver().getResource("/content/carousel");

        CarouselModelV1 model = resource.adaptTo(CarouselModelV1.class);

        assertNotNull(model);

        List<CarouselItem> items = model.getCarouselItems();

        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    void testCarouselItemsNotPresent() {

        Resource resource =
                context.resourceResolver().getResource("/content/emptyCarousel");

        CarouselModelV1 model = resource.adaptTo(CarouselModelV1.class);

        assertNotNull(model);

        assertNull(model.getCarouselItems());
    }
}