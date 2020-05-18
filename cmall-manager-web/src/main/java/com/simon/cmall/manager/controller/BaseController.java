package com.simon.cmall.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.simon.cmall.bean.UmsMember;
import com.simon.cmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("admin")
public class BaseController {

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping("index")
    public String index() {
        return "index";
    }


    @RequestMapping("welcome")
    public String welcome() {
        return "welcome";
    }


    @RequestMapping("catalog")
    public String catalog() {
        return "catalog";
    }

    @RequestMapping("catalog1")
    public String catalog1() {
        return "catalog1";
    }

    @RequestMapping("catalog2")
    public String catalog2() {
        return "catalog2";
    }

    @RequestMapping("catalog3")
    public String catalog3() {
        return "catalog3";
    }


    @RequestMapping("spumanager")
    public String spumanager() {
        return "spumanager";
    }


    @RequestMapping("skumanager")
    public String skumanager() {
        return "skumanager";
    }

    @RequestMapping("skulistpage")
    public String skulistpage() {
        return "skulistpage";
    }

    @RequestMapping("addskupage")
    public String addskupage(String skuId) {
        return "addsku";
    }


    @RequestMapping("usermanager")
    public String usermanager() {
        return "usermanager";
    }

    @RequestMapping("usermanagerweibo")
    public String usermanagerweibo() {
        return "usermanagerweibo";
    }


    @RequestMapping("ordermanager")
    public String ordermanager() {
        return "ordermanager";
    }


    @Reference
    UserService userService;

    @RequestMapping("/getAllOriUser")
    @ResponseBody
    public List<UmsMember> getAllOriUser() {
        List<UmsMember> umsMembers = userService.getAllOriUser();
        return umsMembers;
    }

}
