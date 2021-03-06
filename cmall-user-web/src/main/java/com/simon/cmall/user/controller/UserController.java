package com.simon.cmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.simon.cmall.bean.UmsMember;
import com.simon.cmall.bean.UmsMemberReceiveAddress;
import com.simon.cmall.service.UmsMemberReceiveAddressService;
import com.simon.cmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class UserController {
    @Reference
    private UserService userService;
    @Reference
    private UmsMemberReceiveAddressService umsMemberReceiveAddressService;

    @RequestMapping("/index")
    @ResponseBody
    public String index(){
        return "hello index";
    }

    @RequestMapping("/getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(){
      List<UmsMember> umsMembers = userService.getAllUser();
      return umsMembers;
    }

    @RequestMapping("/getAllOriUser")
    @ResponseBody
    public List<UmsMember> getAllOriUser(){
        List<UmsMember> umsMembers = userService.getAllOriUser();
        return umsMembers;
    }


    @RequestMapping("/getAllSocUser")
    @ResponseBody
    public List<UmsMember> getAllSocUser(){
        List<UmsMember> umsMembers = userService.getAllSocUser();
        return umsMembers;
    }

    @RequestMapping("/getReceiveAddressByMemberId")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId){
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressService.getReceiveAddressByMemberId(memberId);
        return umsMemberReceiveAddresses;
    }
}
