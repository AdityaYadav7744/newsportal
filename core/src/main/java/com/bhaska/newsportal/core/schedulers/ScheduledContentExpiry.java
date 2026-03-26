package com.bhaska.newsportal.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface ScheduledContentExpiry {

    @AttributeDefinition
    public String cronExpression() default "* * ? * * * ";

    @AttributeDefinition
    public boolean status() default true;
}
