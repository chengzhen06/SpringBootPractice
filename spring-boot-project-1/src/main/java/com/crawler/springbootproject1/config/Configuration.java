package com.crawler.springbootproject1.config;


import com.crawler.springbootproject1.component.LoginHandlerInterceptor;
import com.crawler.springbootproject1.component.MyLocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@org.springframework.context.annotation.Configuration
public class Configuration implements WebMvcConfigurer  {



    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/index").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/charts").setViewName("charts");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/","/index","/register","/loginCheck","/css/**","/webjars/**");
    }

    @Bean
    public LocaleResolver localeResolver()
    {
        return new MyLocaleResolver();
    }
}
