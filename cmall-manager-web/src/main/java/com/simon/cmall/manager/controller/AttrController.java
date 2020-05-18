package com.simon.cmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.simon.cmall.bean.PmsBaseAttrInfo;
import com.simon.cmall.bean.PmsBaseAttrValue;
import com.simon.cmall.bean.PmsBaseSaleAttr;
import com.simon.cmall.manager.util.PmsUploadUtil;
import com.simon.cmall.service.AttrService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class AttrController  {

    @Reference
    AttrService attrService;

    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file")MultipartFile multipartFile){
//        将图片或者音频视频上传到分布式的文件存储文件系统
//        将图片的存储路径返回给页面
        String filename = multipartFile.getOriginalFilename();

        String imgUrl = PmsUploadUtil.uploadImage(multipartFile);
        System.out.println(imgUrl);

        JSONObject jsonObject = new JSONObject();
        JSONObject resUrl = new JSONObject();
        try {
            resUrl.put("src",imgUrl);
            resUrl.put("filename",filename);
            jsonObject.put("code",0);
            jsonObject.put("msg","成功上传");
            jsonObject.put("data",resUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = jsonObject.toString();
        return s;
    }


    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<PmsBaseSaleAttr> attrInfoList(){

        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = attrService.baseSaleAttrList();
        return pmsBaseSaleAttrs;
    }

    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo, String valueName){

        String[] split = valueName.split(",");
        List<PmsBaseAttrValue> pmsBaseAttrValues = new ArrayList<>();
        for (String s : split) {
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setValueName(s);
            pmsBaseAttrValues.add(pmsBaseAttrValue);
        }
        pmsBaseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        String success = attrService.saveAttrInfo(pmsBaseAttrInfo);
        return success;
    }

    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id){

        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.attrInfoList(catalog3Id);
        return pmsBaseAttrInfos;
    }

    @RequestMapping("getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList(String attrId){
        List<PmsBaseAttrValue> pmsBaseAttrValues = attrService.getAttrValueList(attrId);
        return pmsBaseAttrValues;
    }
}
