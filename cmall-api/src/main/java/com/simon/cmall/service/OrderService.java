package com.simon.cmall.service;


import com.simon.cmall.bean.OmsOrder;


public interface OrderService {
    String checkOrder(String memberId, String tradeCode);

    String genTradeCode(String memberId);


    void saveOrder(OmsOrder omsOrder);
}
