package com.bhaska.newsportal.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ArticelDetailsClassTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ArticelDetailsClass.class);
        context.load().json("/articleDetails.json", "/content");
    }

    @Test
    void testArticleDetails_AllProperties() {
        context.currentResource("/content/article1");
        ArticelDetailsClass model = context.currentResource().adaptTo(ArticelDetailsClass.class);
        assertNotNull(model);

        assertEquals("Breaking News", model.getArticleTitle());
        assertEquals("This is a breaking news article", model.getArticelDesc());
        assertEquals("/content/dam/news/article1.jpg", model.getArticleImage());
        assertEquals("2026-03-18", model.getArticelDate());
    }

    @Test
    void testArticleDetails_EmptyProperties() {
        context.currentResource("/content/articleEmpty");
        ArticelDetailsClass model = context.currentResource().adaptTo(ArticelDetailsClass.class);
        assertNotNull(model); // model is not null

        assertEquals("", model.getArticleTitle());
        assertEquals("", model.getArticelDesc());
        assertEquals("", model.getArticleImage());
        assertEquals("", model.getArticelDate());
    }
}