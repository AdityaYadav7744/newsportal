package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class FaqAccordionModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {

        context.addModelsForClasses(
                FaqAccordionModel.class,
                FaqChildAccordioinModel.class
        );

        context.load().json("/faqaccordionmodel.json", "/content");
    }

    @Test
    void testAccordionItemsPresent() {

        Resource resource =
                context.resourceResolver().getResource("/content/faq");

        assertNotNull(resource);

        FaqAccordionModel model = resource.adaptTo(FaqAccordionModel.class);

        assertNotNull(model);

        List<FaqChildAccordioinModel> items = model.getAccordionItems();

        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    void testAccordionItemsAbsent() {

        Resource resource =
                context.resourceResolver().getResource("/content/emptyFaq");

        FaqAccordionModel model = resource.adaptTo(FaqAccordionModel.class);

        assertNotNull(model);

        assertNull(model.getAccordionItems());
    }
}