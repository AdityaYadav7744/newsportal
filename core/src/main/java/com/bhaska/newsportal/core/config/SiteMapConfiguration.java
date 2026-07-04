package com.bhaska.newsportal.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface SiteMapConfiguration {

    @AttributeDefinition
    String pagePath() default "/content/newsportal/us/en/home-page";

    @AttributeDefinition
    String cronExp () default "30 * * * * ?";

    @AttributeDefinition
    boolean status() default false;
}
