package com.simon.cmall.order.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.simon.cmall.bean.OmsOrder;
import com.simon.cmall.bean.OmsOrderItem;
import com.simon.cmall.mq.ActiveMQUtil;
import com.simon.cmall.order.mapper.OmsOrderItemMapper;
import com.simon.cmall.order.mapper.OmsOrderMapper;
import com.simon.cmall.service.CartService;
import com.simon.cmall.service.OrderService;
import com.simon.cmall.util.RedisUtil;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

//    @Autowired
//    RedisUtil redisUtil;
//    @Autowired
//    OmsOrderMapper omsOrderMapper;
//    @Autowired
//    OmsOrderItemMapper omsOrderItemMapper;
//
//    @Reference
//    CartService cartService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OmsOrderMapper omsOrderMapper;

    @Autowired
    OmsOrderItemMapper omsOrderItemMapper;

    @Autowired
    ActiveMQUtil activeMQUtil;

    @Reference
    CartService cartService;
//
//    @Override
//    public String checkOrder(String memberId, String tradeCode) {
//        Jedis jedis = null;
//        try {
//            jedis = redisUtil.getJedis();
//            String tradeKey = "user:" + memberId + ":tradeCode";
//            //查到就删掉 ui脚本
//            //String tradeCodeFromCache = jedis.get(tradeKey);
//            //对比防重删令牌
//            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//
//            Long eval = (Long) jedis.eval(script, Collections.singletonList(tradeKey),
//                    Collections.singletonList(tradeCode));
//
//
//            if (eval != null && eval != 0) {
//                //使用lua脚本在发现key的同时删除key，防止并发订单攻击
////                jedis.del(tradeKey);
//                return "success";
//            } else {
//                return "fail";
//            }
//        } finally {
//            jedis.close();
//        }
//    }
//
//    @Override
//    public String checkTradeCode(String memberId, String tradeCode) {
//        return null;
//    }
//
//    @Override
//    public String genTradeCode(String memberId) {
//        Jedis jedis = redisUtil.getJedis();
//        String tradeKey = "user:" + memberId + ":tradeCode";
//        String tradeCode = UUID.randomUUID().toString();
//
//        jedis.setex(tradeKey, 15 * 60, tradeCode);
//
//        jedis.close();
//        return tradeCode;
//    }
//
//
//    @Override
//    public void saveOrder(OmsOrder omsOrder) {
////        保存订单表
//        omsOrderMapper.insertSelective(omsOrder);
//        String orderId = omsOrder.getId();
////        保存订单详情
//        List<OmsOrderItem> omsOrderItems = omsOrder.getOmsOrderItems();
//        for (OmsOrderItem omsOrderItem : omsOrderItems) {
//            omsOrderItem.setOrderId(orderId);
//            omsOrderItemMapper.insertSelective(omsOrderItem);
////            删除购物车数据
////            cartService.delCart()
//
//        }
//
//    }
//
//    @Override
//    public OmsOrder getOrderByOutTradeNo(String outTradeNo) {
//        return null;
//    }
//
//    @Override
//    public void updateOrder(OmsOrder omsOrder) {
//
//    }

    @Override
    public String checkTradeCode(String memberId, String tradeCode) {
        Jedis jedis = null ;

        try {
            jedis = redisUtil.getJedis();
            String tradeKey = "user:" + memberId + ":tradeCode";


            //String tradeCodeFromCache = jedis.get(tradeKey);// 使用lua脚本在发现key的同时将key删除，防止并发订单攻击
            //对比防重删令牌
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Long eval = (Long) jedis.eval(script, Collections.singletonList(tradeKey), Collections.singletonList(tradeCode));

            if (eval!=null&&eval!=0) {
                jedis.del(tradeKey);
                return "success";
            } else {
                return "fail";
            }
        }finally {
            jedis.close();
        }

    }

    @Override
    public String genTradeCode(String memberId) {

        Jedis jedis = redisUtil.getJedis();

        String tradeKey = "user:"+memberId+":tradeCode";

        String tradeCode = UUID.randomUUID().toString();

        jedis.setex(tradeKey,60*15,tradeCode);

        jedis.close();

        return tradeCode;
    }

    @Override
    public void saveOrder(OmsOrder omsOrder) {

        // 保存订单表
        omsOrderMapper.insertSelective(omsOrder);
        String orderId = omsOrder.getId();
        // 保存订单详情
        List<OmsOrderItem> omsOrderItems = omsOrder.getOmsOrderItems();
        for (OmsOrderItem omsOrderItem : omsOrderItems) {
            omsOrderItem.setOrderId(orderId);
            omsOrderItemMapper.insertSelective(omsOrderItem);
            // 删除购物车数据
             cartService.delCart();
        }
    }

    @Override
    public OmsOrder getOrderByOutTradeNo(String outTradeNo) {

        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSn(outTradeNo);
        OmsOrder omsOrder1 = omsOrderMapper.selectOne(omsOrder);

        return omsOrder1;
    }

    @Override
    public void updateOrder(OmsOrder omsOrder) {
        Example e = new Example(OmsOrder.class);
        e.createCriteria().andEqualTo("orderSn",omsOrder.getOrderSn());

        OmsOrder omsOrderUpdate = new OmsOrder();

        omsOrderUpdate.setStatus("1");

        // 发送一个订单已支付的队列，提供给库存消费
        Connection connection = null;
        Session session = null;
        try{
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
            Queue payhment_success_queue = session.createQueue("ORDER_PAY_QUEUE");
            MessageProducer producer = session.createProducer(payhment_success_queue);
            //TextMessage textMessage=new ActiveMQTextMessage();//字符串文本
            MapMessage mapMessage = new ActiveMQMapMessage();// hash结构

            

            omsOrderMapper.updateByExampleSelective(omsOrderUpdate,e);
            producer.send(mapMessage);
            session.commit();
        }catch (Exception ex){
            // 消息回滚
            try {
                session.rollback();
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                connection.close();
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        }

    }

    @Override
    public List<OmsOrder> getAllOrder() {
        List<OmsOrder> omsOrders = omsOrderMapper.selectAll();
        return  omsOrders;
    }


}
