package com.simon.cmall.service;





import com.simon.cmall.bean.PmsBaseAttrInfo;
import com.simon.cmall.bean.PmsBaseAttrValue;
import com.simon.cmall.bean.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;


public interface AttrService {
    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueSet);
}
