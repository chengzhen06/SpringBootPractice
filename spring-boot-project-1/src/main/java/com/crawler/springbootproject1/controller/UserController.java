package com.crawler.springbootproject1.controller;

import com.crawler.springbootproject1.bean.User;
import com.crawler.springbootproject1.exception.UserNotExistException;
import dbms.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/users/{page}")
    public String getUserByPage(@PathVariable int page, Model model){
        int dataPerPage = 10;
        Page<User> users = userRepo.findAll(PageRequest.of(page,dataPerPage));
        long count = userRepo.count();
        model.addAttribute("users", users);
        model.addAttribute("pages", (count+9)/dataPerPage);
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
    public String addUser(User user, Map<String, Object> error, Model model){
        if(userRepo.findByUsername(user.getUsername()) != null){
            error.put("usernameExist","usernameExist");
            model.addAttribute("user", user);
            return toAddPage();
        }
        user.setId(Integer.toString((int)(Math.random()*1000000)));
        user.setRegisterDate(new Date());
        userRepo.save(user);
        return "redirect:/users/0";
    }

    @PutMapping("/user/{id}")
    public String editUser(User user){
        User oldUser = userRepo.findById(user.getId());
        user.setRegisterDate(oldUser.getRegisterDate());
        userRepo.save(user);
        return "redirect:/users/0";
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") String id){
        User user = userRepo.findById(id);
        userRepo.delete(user);
        return "redirect:/users/0";
    }
}
