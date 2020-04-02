package com.simon.cmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.simon.cmall.bean.PmsSkuInfo;
import com.simon.cmall.service.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class SkuController {
    @Reference
    SkuService skuService;

    @RequestMapping("skuList")
    @ResponseBody
    public List<PmsSkuInfo> skuList(String spuId) {
        List<PmsSkuInfo> pmsSkuInfos =  skuService.skuList(spuId);
        return pmsSkuInfos;
    }

    @RequestMapping("saveSkuInfo")
    @ResponseBody
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo
            ,String skuAttrValueList_json
            ,String skuSaleAttrValueList_json
            ,String checkboxs) {
//        skuService.saveSkuInfo(pmsSkuInfo);
        return "success";
    }
}
