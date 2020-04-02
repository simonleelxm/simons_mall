package com.simon.cmall.user.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.simon.cmall.bean.UmsMemberReceiveAddress;
import com.simon.cmall.service.UmsMemberReceiveAddressService;
import com.simon.cmall.user.mapper.UmsMemberReceiveAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UmsMemberReceiveAddressServiceImpl implements UmsMemberReceiveAddressService {
    @Autowired
    private UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
//        Example e = new Example(UmsMemberReceiveAddress.class);
//        e.createCriteria().andEqualTo("memberId",memberId);
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
//        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.selectByExample(umsMemberReceiveAddress);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress );

        return umsMemberReceiveAddresses;
    }
}
