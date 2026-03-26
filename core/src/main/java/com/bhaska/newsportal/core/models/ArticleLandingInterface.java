package com.bhaska.newsportal.core.models;

import org.apache.sling.models.annotations.Model;
import org.osgi.service.metatype.annotations.Designate;

import java.util.List;


public interface ArticleLandingInterface {
    public String getRootPath();
    public int getMaxArticles();

}
