package ru.itis.master.party.dormdeals.utils;

import org.springframework.stereotype.Component;

@Component
public class ResourceUrlResolver {

    public String resolveUrl(String id, ResourceType type) {
        //todo make real realization
        return "/resource/test/url/12451";
    }
}
