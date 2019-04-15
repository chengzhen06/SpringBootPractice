package com.crawler.springbootproject1.component;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Configuration
public class MyLocaleResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        Locale locale = httpServletRequest.getLocale();
        String l = httpServletRequest.getParameter("l");
        if(!StringUtils.isEmpty(l)){
            httpServletRequest.getSession().setAttribute("locale",l);
            String[] split = l.split("_");
            locale = new Locale(split[0],split[1]) ;
        }
        else{
            l = (String)httpServletRequest.getSession().getAttribute("locale");
            if(!StringUtils.isEmpty(l)){
                String[] split = l.split("_");
                locale = new Locale(split[0],split[1]) ;
            }
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
