package com.itheima.controller;

import com.itheima.domain.SysRole;
import com.itheima.domain.SysUser;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/findAll")
    public String findAll(Model model){
        List<SysUser> list = userService.findAll();
        model.addAttribute("list", list);
        return "user-list";
    }

    @RequestMapping("/save")
    public String save(SysUser user){
        userService.save(user);
        return "redirect:findAll";
    }

    @RequestMapping("/findDetail")
    public String findDetail(Model model, String id){
        SysUser user = userService.findById(id);
        model.addAttribute("user", user);
        return "user-show";
    }


    @RequestMapping("/toUserAddRolePage")
    public String toUserAddRolePage(Model model, String id){
        List<SysRole> roleList = userService.toUserAddRolePage(id);
        model.addAttribute("uid", id);
        model.addAttribute("roleList", roleList);
        return "user-role-add";
    }


    @RequestMapping("/addRoleToUser")
    public String addRoleToUser(String userId, String[] ids){
        userService.addRoleToUser(userId, ids);
        return "redirect:findDetail?id="+userId;
    }

    @RequestMapping("/getUsername")
    public void getUsername(HttpServletRequest request, HttpServletResponse response){
        //获取当前认证通过的用户名方式一：
        String name1 = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(name1);
        //获取当前认证通过的用户名方式二：
        String name2 = request.getRemoteUser();
        System.out.println(name2);
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(name1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
