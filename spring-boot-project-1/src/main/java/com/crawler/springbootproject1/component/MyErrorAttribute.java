package com.crawler.springbootproject1.component;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class MyErrorAttribute extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        Map<String, Object> ext = (Map<String, Object>)webRequest.getAttribute("ext", 0);
        errorAttributes.put("ext",ext);
        return errorAttributes;
    }

    @Override
    public Throwable getError(WebRequest webRequest) {
        return null;
    }
}
