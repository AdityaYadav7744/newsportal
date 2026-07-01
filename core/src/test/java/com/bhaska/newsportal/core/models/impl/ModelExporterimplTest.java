package com.bhaska.newsportal.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.bhaska.newsportal.core.models.ModelExporter;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class ModelExporterimplTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {

        context.addModelsForClasses(ModelExporterimpl.class);

        context.load().json("/modelexporter.json", "/content");

        Resource resource =
                context.resourceResolver().getResource("/content/article");

        context.currentResource(resource);
    }

    @Test
    void testModelExporter() {

        ModelExporter model =
                context.currentResource().adaptTo(ModelExporter.class);

        assertNotNull(model);

        assertEquals("AEM Exporter", model.getArticleTitle());
        assertEquals("Testing Sling Model Exporter", model.getArticelDesc());
        assertEquals("/content/dam/newsportal/article.png",
                model.getArticleImage());
        assertEquals("2026-06-30", model.getArticelDate());
    }
}