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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class UserController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/users/{page}")
    public String getUserByPage(@PathVariable int page, Model model, HttpServletRequest httpServletRequest){
        int dataPerPage = 10;
        Page<User> users = userRepo.findAll(PageRequest.of(page,dataPerPage));
        long count = userRepo.count();
        Optional<String> id = Optional.ofNullable(httpServletRequest.getParameter("id"));
        Optional<String> username = Optional.ofNullable(httpServletRequest.getParameter("username"));
        Optional<String> name = Optional.ofNullable(httpServletRequest.getParameter("name"));
        Optional<String> email = Optional.ofNullable(httpServletRequest.getParameter("email"));
       // Optional<Date> regDate = Optional.ofNullable(httpServletRequest.getParameter("regDate"));
        Optional<String> comment = Optional.ofNullable(httpServletRequest.getParameter("comment"));
        BiPredicate<Boolean, Boolean> method = (b1, b2) -> b1&b2;
        List<User> filterUsers = users.stream()
                .filter(u -> {
                    boolean flag = true;
                    flag = method.test(flag, u.getId().contains(id.orElse("")));
                    flag = method.test(flag, u.getUsername().contains(username.orElse("")));
                    flag = method.test(flag, u.getName().contains(name.orElse("")));
                    flag = method.test(flag, u.getEmail().contains(email.orElse("")));
                    flag = method.test(flag, u.getComment().contains(comment.orElse("")));
                    System.out.println(u);
                    return flag;
                })
                .collect(Collectors.toList());
        model.addAttribute("users", filterUsers);
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
