package com.crawler.springbootproject1.controller;

import com.crawler.springbootproject1.bean.User;
import com.crawler.springbootproject1.exception.UserNotExistException;
import dbms.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/users")
    public String getUserByPage(Model model){
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "user/tables";
    }

    @GetMapping("/user")
    public String toAddPage()
    {
        return "user/add";
    }

    @GetMapping("/user/{id}")
    public String toEditPage(@PathVariable("id") String id, Model model){
        User user = userRepo.findById(id);
        if(user == null)
            throw new UserNotExistException();
        model.addAttribute("user",user);
        return "user/edit";
    }

    @PostMapping("/user")
    public String addUser(User user){
        user.setId(Integer.toString((int)(Math.random()*1000000)));
        user.setRegisterDate(new Date());
        userRepo.save(user);
        return "redirect:/users";
    }

    @PutMapping("/user/{id}")
    public String editUser(User user){
        User oldUser = userRepo.findById(user.getId());
        user.setRegisterDate(oldUser.getRegisterDate());
        userRepo.save(user);
        return "redirect:/users";
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") String id){
        User user = userRepo.findById(id);
        userRepo.delete(user);
        return "redirect:/users";
    }
}
