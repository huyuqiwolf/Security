package com.ken.study.security.file.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 首页
 */
@Controller
@RequestMapping(value = "/mvc")
public class IndexController {

    @RequestMapping(value = "/index")
    public String index(){
        System.out.println("index");
        return "index";
    }

}
