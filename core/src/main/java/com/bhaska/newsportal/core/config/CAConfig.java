package com.bhaska.newsportal.core.config;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration
public @interface CAConfig {

    @Property
    String siteName();

    @Property
    String language();
}
