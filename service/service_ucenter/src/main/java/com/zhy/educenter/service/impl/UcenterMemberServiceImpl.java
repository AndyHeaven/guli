package com.zhy.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhy.commonutils.JwtUtils;
import com.zhy.commonutils.MD5;
import com.zhy.educenter.entity.UcenterMember;
import com.zhy.educenter.entity.vo.RegisterVo;
import com.zhy.educenter.mapper.UcenterMemberMapper;
import com.zhy.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.servicebase.exceptionhandler.GuliException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-21
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String login(UcenterMember member) {

        String mobile = member.getMobile();
        String password = member.getPassword();
        //判空
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "登录失败（手机号或密码为空）！");
        }
        //判断手机号正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        if (mobileMember == null) {
            throw new GuliException(20001, "手机号不存在！");
        }
        //判断密码,因为前端传递过来的密码肯定是源密码，后段数据库储存肯定要考虑安全性会进行，加密，所有要对前端传递过来的数据进行加密在比较
        String selectPassword = mobileMember.getPassword();
        if (!MD5.encrypt(password).equals(selectPassword)) {
            throw new GuliException(20001, "密码错误！");
        }
        //判断用户是否被禁用 is_disabled
        if (mobileMember.getIsDisabled()) {
            throw new GuliException(20001, "用户已被封禁！");
        }
        //根据id and nickname 生成token
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
        return jwtToken;
    }

    @Override
    public void register(RegisterVo registerVo) {

        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

        //判空
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname)) {
            throw new GuliException(20001, "注册失败（注册关键信息为空）！");
        }

        //判断验证码
        if (!redisTemplate.opsForValue().get(mobile).equals(code)){
            throw new GuliException(20001, "注册失败（验证码错误）！");
        }

        //手机号不能重复注册
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        if (ucenterMember !=null){
            throw new GuliException(20001, "注册失败（手机号已注册）！");
        }

        //添加数据库
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("https://edu-zhy.oss-cn-hangzhou.aliyuncs.com/default.jpg");
        baseMapper.insert(member);
    }

    //根据openid判断
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    @Override
    public Integer countRegister(String day) {
        return baseMapper.countRegister(day);
    }
}
