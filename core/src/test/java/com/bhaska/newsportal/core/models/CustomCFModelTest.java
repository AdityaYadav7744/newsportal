package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomCFModelTest {

    private CustomCFModel model;

    private Resource resource;
    private ContentFragment contentFragment;
    private ContentElement titleElement;
    private ContentElement descriptionElement;

    @BeforeEach
    void setUp() {

        model = new CustomCFModel();

        resource = mock(Resource.class);
        contentFragment = mock(ContentFragment.class);
        titleElement = mock(ContentElement.class);
        descriptionElement = mock(ContentElement.class);

        model.setResource(resource);
    }

    @Test
    void testInit() throws Exception {

        when(resource.adaptTo(ContentFragment.class))
                .thenReturn(contentFragment);

        when(contentFragment.getElement("title"))
                .thenReturn(titleElement);

        when(contentFragment.getElement("textArea"))
                .thenReturn(descriptionElement);

        when(titleElement.getContent())
                .thenReturn("News Portal");

        when(descriptionElement.getContent())
                .thenReturn("This is a content fragment.");

        Method method = CustomCFModel.class.getDeclaredMethod("init");
        method.setAccessible(true);
        method.invoke(model);

        assertEquals("News Portal", model.getTitle());
        assertEquals("This is a content fragment.", model.getDescription());
    }
}