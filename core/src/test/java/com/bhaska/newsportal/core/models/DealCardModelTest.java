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
class DealCardModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {

        context.addModelsForClasses(DealCardModel.class);

        context.load().json("/dealcardmodel.json", "/content");

        Resource resource =
                context.resourceResolver().getResource("/content/dealCard");

        context.currentResource(resource);
    }

    @Test
    void testDealCardModel() {

        DealCardModel model =
                context.currentResource().adaptTo(DealCardModel.class);

        assertNotNull(model);

        assertEquals("Premium", model.getSelectedDeal());
        assertEquals("Summer Offer", model.getCardTitle());
        assertEquals("Get flat 50% off on all products.",
                model.getCardDescription());
        assertEquals("/content/dam/newsportal/summer-offer.png",
                model.getCardImageReference());
        assertEquals("summer-offer.png",
                model.getCardImageName());
    }
}