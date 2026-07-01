package com.bhaska.newsportal.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name="Unpblish PageScheduler Configuratino")
public @interface UnpblishPageSchedulerConfiguratino {

    @AttributeDefinition
    String configuration () default "1 * * * * ?";
    @AttributeDefinition
    boolean status () default true;
}
