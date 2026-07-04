package com.bhaska.newsportal.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name="Unpblish PageScheduler Configuratino")
public @interface UnpblishPageSchedulerConfig {

    @AttributeDefinition
    String cron () default "50 * * * * ?";

    @AttributeDefinition
    String path () default "/content/newsportal";

    @AttributeDefinition
    boolean status () default false;
}
