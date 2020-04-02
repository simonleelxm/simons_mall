package com.simon.cmall.service;


import com.simon.cmall.bean.PmsSearchParam;
import com.simon.cmall.bean.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
