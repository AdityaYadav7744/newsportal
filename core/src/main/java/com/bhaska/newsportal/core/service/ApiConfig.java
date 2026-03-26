package com.bhaska.newsportal.core.service;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "External API Configuration",
        description = "Configuration for External API Service"
)
public @interface ApiConfig {

    @AttributeDefinition(name = "API URL")
    String apiUrl();

    @AttributeDefinition(name = "Enable API")
    boolean enableApi() default false;

}