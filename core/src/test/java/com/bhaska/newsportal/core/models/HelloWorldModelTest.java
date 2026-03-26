package com.bhaska.newsportal.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class HelloWorldModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(HelloWorldModel.class);

        // Load JSON
        context.load().json("/helloworld.json", "/content");

        // Create a page (IMPORTANT)
        context.create().page("/content/sample-page");

        // Set current resource under that page
        context.currentResource("/content/testResource");
    }

    @Test
    void testMessage_WithResourceType() {

        HelloWorldModel model =
                context.currentResource().adaptTo(HelloWorldModel.class);

        assertNotNull(model);

        String message = model.getMessage();

        assertTrue(message.contains("Hello World!"));
        assertTrue(message.contains("bhaska/components/content/test"));
        assertFalse(message.contains("/content/sample-page"));
    }

    @Test
    void testMessage_NoResourceType_Default() {

        context.currentResource("/content/noResourceType");

        HelloWorldModel model =
                context.currentResource().adaptTo(HelloWorldModel.class);

        assertNotNull(model);

        assertTrue(model.getMessage().contains("No resourceType"));
    }

    @Test
    void testMessage_NoPageScenario() {

        // Resource outside page
        context.create().resource("/no-page-resource",
                "sling:resourceType", "test/type");

        context.currentResource("/no-page-resource");

        HelloWorldModel model =
                context.currentResource().adaptTo(HelloWorldModel.class);

        assertNotNull(model);

        String message = model.getMessage();

        // Page path should be empty
        assertTrue(message.contains("Current page is:"));
    }
}