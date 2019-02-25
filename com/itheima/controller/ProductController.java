package com.itheima.controller;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @InitBinder
    public void initBinder(WebDataBinder binder){
        //参数一：表示当前要封装的pojo中哪个类型的属性需要封装
        //参数二：具体的封装过程
        binder.registerCustomEditor(Date.class, new PropertiesEditor(){
            //参数表示，页面传过来的参数
            @Override
            public void setAsText(@Nullable String text) throws IllegalArgumentException {
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(text);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //给要封装的属性设置值
                setValue(date);
            }
        });
    }

    @RequestMapping("/findAll")
    public String findAll(Model model,
                          @RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "3") Integer pageSize){
        List<Product> list = productService.findAll(pageNum, pageSize);
        PageInfo<Product> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo", pageInfo);
        return "product-list";
    }

    @RequestMapping("/save")
    public String save(Product product){
        productService.save(product);
        return "redirect:findAll";
    }

    @RequestMapping("/toEditPage")
    public String toEditPage(Model model, String id){
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product-update";
    }

    @RequestMapping("/update")
    public String update(Product product){
        productService.update(product);
        return "redirect:findAll";
    }

    @RequestMapping("/batchDelData")
    public String batchDelData(String[] ids){
        productService.batchDelData(ids);
        return "redirect:findAll";
    }
}
