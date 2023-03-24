package com.zhy.educenter.service;

import com.zhy.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-03-21
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember member);

    void register(RegisterVo registerVo);

    //根据openid判断
    UcenterMember getOpenIdMember(String openid);

    Integer countRegister(String day);
}
