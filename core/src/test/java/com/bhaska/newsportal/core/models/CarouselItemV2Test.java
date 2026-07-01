package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class CarouselItemV2Test {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {

        context.addModelsForClasses(CarouselItemV2.class);

        context.load().json("/carouselitemv2.json", "/content");

        Resource resource = context.resourceResolver()
                .getResource("/content/item");

        context.currentResource(resource);
    }

    @Test
    void testCarouselItemV2() {

        CarouselItemV2 model =
                context.request().adaptTo(CarouselItemV2.class);

        assertNotNull(model);

        assertEquals("image", model.getMediaType());
        assertEquals("/content/dam/newsportal/banner.jpg", model.getImage());
        assertEquals("/content/dam/newsportal/video.mp4", model.getVideoPath());
        assertEquals("Welcome Banner", model.getTitle());
        assertEquals("Read More", model.getButtonText());
        assertEquals("/content/newsportal/home", model.getButtonLink());
    }
}