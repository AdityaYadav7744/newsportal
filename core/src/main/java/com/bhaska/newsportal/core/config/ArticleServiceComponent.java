package com.bhaska.newsportal.core.service;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface ArticleServiceComponent {

    @AttributeDefinition(name="Article Service API")
    public String articleAPI();

    @AttributeDefinition
    public boolean activate();
}
