package com.simon.cmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

import com.simon.cmall.bean.UmsMember;
import com.simon.cmall.bean.UmsMemberReceiveAddress;
import com.simon.cmall.service.UserService;
import com.simon.cmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.simon.cmall.user.mapper.UserMapper;
import com.simon.cmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;
    @Autowired
    RedisUtil redisUtil;


    @Autowired
    public List<UmsMember> getAllUser() {
        List<UmsMember> umsMembers = userMapper.selectAll();
//        List<UmsMember> umsMembers = new ArrayList<>();
//        UmsMember u = new UmsMember();
//        u.setCity("广东");
//        umsMembers.add(u);
//        System.out.println(umsMembers);
        return umsMembers;
    }

    @Override
    public UmsMember login(UmsMember umsMember) {
        Jedis jedis = null;

        try {
            jedis = redisUtil.getJedis();
//        User:password:info
            if (jedis != null) {
                String umsMemberStr = jedis.get("user:" + umsMember.getPassword()+umsMember.getUsername()+ ":info");
                if (StringUtils.isNotBlank(umsMemberStr)) {
                    //密码成功
                    UmsMember umsMemberFromCache = JSON.parseObject(umsMemberStr, UmsMember.class);
                    return umsMemberFromCache;
                }
            }

            //密码错误、缓存中没有，开启数据库
            UmsMember umsMemberFromDb = loginFromDb(umsMember);
            if (umsMemberFromDb != null) {
                jedis.setex("user:" + umsMemberFromDb.getPassword() +umsMemberFromDb.getUsername()+ ":info", 60 * 60 * 24, JSON.toJSONString(umsMemberFromDb));
            }
            return umsMemberFromDb;

        } finally {


        }

    }

    @Override
    public void addUserToken(String token, String memberId) {
        Jedis jedis = redisUtil.getJedis();
        jedis.setex("user:"+memberId+":token",60*60*2,token);
        jedis.close();

    }

    @Override
    public UmsMember addOauthUser(UmsMember umsMember) {
        userMapper.insertSelective(umsMember);
        return umsMember;
    }

    @Override
    public UmsMember checkOauthUser(UmsMember umsCheck) {
        UmsMember umsMember = userMapper.selectOne(umsCheck);
        return umsMember;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        // 封装的参数对象
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);
//       Example example = new Example(UmsMemberReceiveAddress.class);
//       example.createCriteria().andEqualTo("memberId",memberId);
//       List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.selectByExample(example);

        return umsMemberReceiveAddresses;
    }

    @Override
    public UmsMemberReceiveAddress getReceiveAddressById(String memberId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setId(memberId);
        UmsMemberReceiveAddress umsMemberReceiveAddress1 = umsMemberReceiveAddressMapper.selectOne(umsMemberReceiveAddress);
        return umsMemberReceiveAddress1;
    }

    @Override
    public List<UmsMember> getAllOriUser() {
        UmsMember umsMember = new UmsMember();
        umsMember.setSourceType("0");
        List<UmsMember> umsMembers = userMapper.select(umsMember);
        return umsMembers;
    }

    @Override
    public List<UmsMember> getAllSocUser() {
        UmsMember umsMember = new UmsMember();
        umsMember.setSourceType("2");
        List<UmsMember> umsMembers = userMapper.select(umsMember);
        return umsMembers;
    }

    private UmsMember loginFromDb(UmsMember umsMember) {
        List<UmsMember> umsMembers = userMapper.select(umsMember);
        if(umsMembers!=null){
            return umsMembers.get(0);
        }
        return null;
    }
}
