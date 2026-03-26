package com.bhaska.newsportal.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "WeeklySiteReportConfig")
public @interface WeeklySiteReportConfig {

    @AttributeDefinition(name = "Enter a Cron Expression")
    String cronExpression() default "0 0 0 ? SUN * *";

    @AttributeDefinition(name= "State")
    boolean state () default true;

}
