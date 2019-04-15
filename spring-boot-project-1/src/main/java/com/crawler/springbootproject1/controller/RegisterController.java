package com.crawler.springbootproject1.controller;

import com.crawler.springbootproject1.bean.User;
import dbms.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class RegisterController {
    @Autowired
    UserRepo userRepo;

    @RequestMapping("/registered")
    public String registered(HttpServletRequest httpServletRequest){
        User user = new User();
        user.setId(Integer.toString((int)(Math.random()*1000000)));
        user.setUsername(httpServletRequest.getParameter("username"));
        user.setPassword(httpServletRequest.getParameter("password"));
        user.setEmail(httpServletRequest.getParameter("email"));
        user.setName(httpServletRequest.getParameter("name"));
        user.setRegisterDate(new Date());
        userRepo.save(user);
        return "login";
    }
}
