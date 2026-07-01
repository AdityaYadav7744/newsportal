package com.bhaska.newsportal.core.service;

import java.util.List;

public interface CountryStateService {
    public List<String> getStates(String country);
    public List<String> getFallback(String country);
}
