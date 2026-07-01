package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class CarouselItemTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {

        context.addModelsForClasses(CarouselItem.class);

        context.load().json("/carouselitem.json", "/content");
    }

    @Test
    void testCarouselItemModel() {

        Resource resource = context.resourceResolver()
                .getResource("/content/item");

        assertNotNull(resource);

        CarouselItem item = resource.adaptTo(CarouselItem.class);

        assertNotNull(item);

        assertEquals("/content/dam/newsportal/banner1.jpg", item.getImage());
        assertEquals("Welcome Banner", item.getTitle());
        assertEquals("Read More", item.getButtonText());
        assertEquals("/content/newsportal/home", item.getButtonLink());
        assertEquals("image", item.getMediaType());
        assertEquals("/content/dam/newsportal/video.mp4", item.getVideoPath());
    }
}