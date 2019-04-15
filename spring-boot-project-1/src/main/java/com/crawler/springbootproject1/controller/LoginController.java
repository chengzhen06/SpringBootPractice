package com.crawler.springbootproject1.controller;

import com.crawler.springbootproject1.bean.User;
import dbms.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    UserRepo userRepo;

    @PostMapping("/loginCheck")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String, Object> error,
                        HttpSession httpSession)
    {
        User user = userRepo.findByUsername(username);
        if(user == null)
        {
            error.put("errUsername","用户名错误");
            return "login";
        }
        else
        {
            if(user.getPassword().equals(password))
            {
                httpSession.setAttribute("loginUser", user);
                return "home";
            }
            else
            {
                error.put("errPassword","密码错误");
                return "login";
            }
        }
    }
}
