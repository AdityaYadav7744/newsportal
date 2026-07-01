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
class FaqChildAccordioinModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {

        context.addModelsForClasses(FaqChildAccordioinModel.class);

        context.load().json("/faqchildaccordionmodel.json", "/content");

        Resource resource = context.resourceResolver()
                .getResource("/content/faqItem");

        context.currentResource(resource);
    }

    @Test
    void testFaqChildAccordionModel() {

        FaqChildAccordioinModel model = context.currentResource()
                .adaptTo(FaqChildAccordioinModel.class);

        assertNotNull(model);

        assertEquals("What is AEM?", model.getTitle());
        assertEquals("/content/dam/newsportal/aem.png", model.getImage());
        assertEquals("Adobe Experience Manager is a CMS.", model.getDescription());
    }
}