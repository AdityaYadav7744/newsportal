package com.bhaska.newsportal.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface ProductSchedulerConfig {
    @AttributeDefinition(name="Cron Expression")
    String cronExpression() default "*/10 * * ? * *";

    @AttributeDefinition(name = "Scheduler Name")
    String schedulerName() default "ProductScheduler";

    @AttributeDefinition(name = "Enable/Disable Schedular")
    boolean enable() default true;

    @AttributeDefinition(name= "API Url")
    String apiUrl() default "https://fakestoreapi.com/products";

}
