package com.bhaska.newsportal.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface DamMetaDataProcessingConfig {

    @AttributeDefinition(name = "Job Enable")
    boolean jobEnable () default false;

    @AttributeDefinition(name = "Allowed Dam Path")
    String path () default "/content/dam/newsportal";

    @AttributeDefinition(name = "Max Entry Count")
    int maxEntryCount() default 3;


}
