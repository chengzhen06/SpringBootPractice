package com.crawler.springbootproject1.controller;

import com.crawler.springbootproject1.bean.User;
import dbms.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@Controller
public class RegisterController {
    @Autowired
    UserRepo userRepo;

    @PostMapping("/registerCheck")
    public String registered(HttpServletRequest httpServletRequest, Map<String, Object> error){
        User user = new User();
        user.setId(Integer.toString((int)(Math.random()*1000000)));
        user.setUsername(httpServletRequest.getParameter("username"));
        user.setPassword(httpServletRequest.getParameter("password"));
        user.setEmail(httpServletRequest.getParameter("email"));
        user.setName(httpServletRequest.getParameter("name"));
        user.setRegisterDate(LocalDate.now());
        if(userRepo.findByUsername(user.getUsername()) != null){
            error.put("usernameExist","usernameExist");
            return "register";
        }
        userRepo.save(user);
        return "login";
    }
}
