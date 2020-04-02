package com.simon.cmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.simon.cmall.bean.PmsProductSaleAttr;
import com.simon.cmall.bean.PmsSkuInfo;
import com.simon.cmall.bean.PmsSkuSaleAttrValue;
import com.simon.cmall.service.SkuService;
import com.simon.cmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class itemController {
    @Reference
    SkuService skuService;
    @Reference
    SpuService spuService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap map, HttpServletRequest request){
//        直接从请求中获取id
        String remoteAddr = request.getRemoteAddr();


//        sku对象
        PmsSkuInfo pmsSkuInfo =  skuService.getSkuById(skuId,remoteAddr);
        map.addAttribute("skuInfo",pmsSkuInfo);
//        spu销售属性
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),skuId);
        map.addAttribute("spuSaleAttrListCheckBySku",pmsProductSaleAttrs);

//        查询当前的sku的spu和其他sku的集合的hash表
        Map<String,String> skuSaleAttrHash = new HashMap<>();
        List<PmsSkuInfo> pmsSkuInfos = skuService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());

        for (PmsSkuInfo skuInfo : pmsSkuInfos) {
            String k = "";
            String v = skuInfo.getId();
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                k += pmsSkuSaleAttrValue.getSaleAttrValueId()+"|";
            }
            skuSaleAttrHash.put(k,v);
        }
//        存放为json字符串
        String skuSaleAttrHashJsonStr = JSON.toJSONString(skuSaleAttrHash);
        map.put("skuSaleAttrHashJsonStr",skuSaleAttrHashJsonStr);

        return "item";
    }
}
