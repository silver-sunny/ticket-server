package com.kupstudio.bbarge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/", "/index"})
public class IndexController {

    @GetMapping(value = {"/", "/index"})
    public String index(){
        return "index";
    }

}
