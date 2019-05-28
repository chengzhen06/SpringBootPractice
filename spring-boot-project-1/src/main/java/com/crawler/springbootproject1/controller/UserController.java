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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    UserRepo userRepo;
    int dataPerPage = 10;

    @GetMapping("/users/{page}")
    public String getUserByPage(@PathVariable int page, Model model, HttpServletRequest httpServletRequest){
        //Page<User> users = userRepo.findAll(PageRequest.of(page, dataPerPage));
        List<User> filterUsers = filter(model, httpServletRequest);
        long count = filterUsers.size();
        int limit = (page*dataPerPage+dataPerPage)<filterUsers.size()?(page*dataPerPage+dataPerPage):filterUsers.size();
        filterUsers = filterUsers.subList(page*dataPerPage, limit);
        model.addAttribute("users", filterUsers);
        model.addAttribute("pages", (count+9)/dataPerPage);
        model.addAttribute("curPage", page);
        return "user/tables";
    }
    @GetMapping("/users/{page}/{type}")
    public String getUserByPagePHP(@PathVariable int page, @PathVariable String type, Model model, HttpServletRequest httpServletRequest){
        getUserByPage(page, model, httpServletRequest);
        return "user/tablesPHP";
    }

    public List<User> filter(Model model, HttpServletRequest httpServletRequest){
        Optional<String> id = Optional.ofNullable(httpServletRequest.getParameter("id"));
        Optional<String> username = Optional.ofNullable(httpServletRequest.getParameter("username"));
        Optional<String> name = Optional.ofNullable(httpServletRequest.getParameter("name"));
        Optional<String> email = Optional.ofNullable(httpServletRequest.getParameter("email"));
        Optional<String> regDateBefore = Optional.ofNullable(httpServletRequest.getParameter("regDateBefore"));
        Optional<String> regDateAfter = Optional.ofNullable(httpServletRequest.getParameter("regDateAfter"));
        Optional<String> comment = Optional.ofNullable(httpServletRequest.getParameter("comment"));
        Optional<String> filterMethod = Optional.ofNullable(httpServletRequest.getParameter("filterMethod"));
        model.addAttribute("id", id.orElse(""));
        model.addAttribute("username", username.orElse(""));
        model.addAttribute("name", name.orElse(""));
        model.addAttribute("email", email.orElse(""));
        model.addAttribute("regDateBefore", regDateBefore.orElse(""));
        model.addAttribute("regDateAfter", regDateAfter.orElse(""));
        model.addAttribute("comment", comment.orElse(""));
        model.addAttribute("filterMethod", filterMethod.orElse(""));

        BiPredicate<Boolean, Boolean> method;
        BiPredicate<String, String> compareString;
        BiPredicate<LocalDate, LocalDate> dateCompare = (d1, d2) -> d1.isAfter(d2);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(filterMethod.orElse("and").equals("and")){
            method = (b1, b2) -> b1&b2;
            compareString = (s1, s2) -> s1.contains(s2);
        }
        else{
            method = (b1, b2) -> b1|b2;
            compareString = (s1, s2) -> {
                if(s2 == "")
                    return false;
                return s1.contains(s2);
            };
        }

        List<User> filterUsers = userRepo.findAll().stream()
                .filter(u -> {
                    boolean flag = false;
                    if(filterMethod.orElse("and").equals("and"))
                        flag = true;
                    flag = method.test(flag, compareString.test(u.getId(),id.orElse("")));
                    flag = method.test(flag, compareString.test(u.getUsername(), username.orElse("")));
                    flag = method.test(flag, compareString.test(u.getName(), name.orElse("")));
                    flag = method.test(flag, compareString.test(u.getEmail(), email.orElse("")));
                    flag = method.test(flag, compareString.test(u.getComment(), comment.orElse("")));
                    LocalDate dateBefore = LocalDate.parse(regDateBefore.orElse("1970-01-01"), dtf).minusDays(1);
                    LocalDate dateAfter = LocalDate.parse(regDateAfter.orElse("2100-01-01"), dtf).plusDays(1);
                    boolean range = dateCompare.test(u.getRegisterDate(), dateBefore) & dateCompare.test(dateAfter, u.getRegisterDate());
                    flag = method.test(flag, range);
                    return flag;
                })
                .collect(Collectors.toList());
        return filterUsers;
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
        user.setRegisterDate(LocalDate.now());
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
