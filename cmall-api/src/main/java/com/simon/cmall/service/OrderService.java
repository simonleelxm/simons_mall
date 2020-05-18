package com.simon.cmall.service;


import com.simon.cmall.bean.OmsOrder;

import java.util.List;


public interface OrderService {
//    String checkOrder(String memberId, String tradeCode);
//
//    String genTradeCode(String memberId);
//
//
//    void saveOrder(OmsOrder omsOrder);

    String checkTradeCode(String memberId, String tradeCode);

    String genTradeCode(String memberId);

    void saveOrder(OmsOrder omsOrder);

    OmsOrder getOrderByOutTradeNo(String outTradeNo);

    void updateOrder(OmsOrder omsOrder);


    List<OmsOrder> getAllOrder();
}
