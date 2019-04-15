package com.crawler.springbootproject1.controller;


import com.crawler.springbootproject1.exception.UserNotExistException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(UserNotExistException.class)
    public String handleException(Exception e, HttpServletRequest httpServletRequest){
        Map<String, Object> map = new HashMap<>();
        httpServletRequest.setAttribute("javax.servlet.error.status_code", "501");
        map.put("msg",e.getMessage());
        httpServletRequest.setAttribute("ext", map);
        return "forward:/error";
    }
}
