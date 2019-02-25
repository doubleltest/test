package com.itheima.controller;

import com.itheima.domain.Orders;
import com.itheima.domain.PageBean;
import com.itheima.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @RequestMapping("/findAll")
    public String findAll(Model model){
        List<Orders> list = ordersService.findAll();
        model.addAttribute("list", list);
        return "order-list";
    }

    @RequestMapping("/pageQuery")
    public String pageQuery(@RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "3") Integer pageSize){
        PageBean pageBean = ordersService.pageQuery(pageNum, pageSize);
        System.out.println(pageBean);
        return "order-list";
    }

}
