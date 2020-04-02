package com.simon.cmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.simon.cmall.bean.PmsProductImage;
import com.simon.cmall.bean.PmsProductInfo;
import com.simon.cmall.bean.PmsProductSaleAttr;
import com.simon.cmall.bean.PmsProductSaleAttrValue;
import com.simon.cmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
public class SpuController {
    @Reference
    SpuService spuService ;

    @RequestMapping("spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(String spuId){
        List<PmsProductImage> pmsProductImages = spuService.spuImageList(spuId);
        return pmsProductImages;
    }


    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }

    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(PmsProductInfo pmsProductInfo,String basesaleattrname,String imgUrls,String spuSaleAttrLists,String imgnames,String saleAttrName){
        String[] imgUrl_list = imgUrls.split(",");
        String[] imgnames_list = imgnames.split(",");
        String[] saleAttrids = saleAttrName.split(",");
        String[] saleAttrValues = spuSaleAttrLists.split(",");
        String[] basesaleattrnames = basesaleattrname.split(",");
        List<PmsProductImage> pmsProductImages = new ArrayList<>();
        List<PmsProductSaleAttr> pmsProductSaleAttrs = new ArrayList<>();
        for (String s : imgUrl_list) {
                PmsProductImage pmsProductImage = new PmsProductImage();
                pmsProductImage.setImgUrl(s);
                pmsProductImages.add(pmsProductImage);
        }
        for (int i = 0; i < pmsProductImages.size(); i++) {
            pmsProductImages.get(i).setImgName(imgnames_list[i]);
        }
        //attrid
        for (String s : saleAttrids) {
            PmsProductSaleAttr productSaleAttr = new PmsProductSaleAttr();
            productSaleAttr.setSaleAttrId(s);
            pmsProductSaleAttrs.add(productSaleAttr);
        }
        for (int i = 0; i < pmsProductSaleAttrs.size(); i++) {
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = new ArrayList<>();
            String[] saleAttrValues_i = saleAttrValues[i].split(":");
            String saleattrid = pmsProductSaleAttrs.get(i).getId();
            for (String s1 : saleAttrValues_i) {
                PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
                pmsProductSaleAttrValue.setSaleAttrId(saleattrid);
                pmsProductSaleAttrValue.setSaleAttrValueName(s1);
                pmsProductSaleAttrValues.add(pmsProductSaleAttrValue);
            }
            pmsProductSaleAttrs.get(i).setSpuSaleAttrValueList(pmsProductSaleAttrValues);
            pmsProductSaleAttrs.get(i).setSaleAttrName(basesaleattrnames[i]);
        }

        pmsProductInfo.setSpuImageList(pmsProductImages);
        pmsProductInfo.setSpuSaleAttrList(pmsProductSaleAttrs);

        String success = spuService.saveSpuInfo(pmsProductInfo);
        return success;
    }

    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(String catalog3Id){
        List<PmsProductInfo> pmsProductInfos = spuService.spuList(catalog3Id);
        return pmsProductInfos;
    }





}
