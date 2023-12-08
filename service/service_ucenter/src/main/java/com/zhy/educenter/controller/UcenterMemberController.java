package com.zhy.educenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhy.commonutils.JwtUtils;
import com.zhy.commonutils.R;
import com.zhy.commonutils.ordervo.UcenterMemberOrder;
import com.zhy.educenter.entity.UcenterMember;
import com.zhy.educenter.entity.vo.RegisterVo;
import com.zhy.educenter.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-21
 */
@Api(description = "用户管理")
@RestController
@RequestMapping("/educenter/member")
//@CrossOrigin
public class UcenterMemberController {
    @Resource
    private UcenterMemberService ucenterMemberService;

    //登录
    @ApiOperation("用户登录")
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member) {
        String token = ucenterMemberService.login(member);
        return R.ok().data("token", token);
    }

    @ApiOperation("用户注册")
    //注册
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo) {
        ucenterMemberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation("根据token获取用户信息")
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request) {
        //调用jwt工具类方法
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember member = ucenterMemberService.getById(memberId);
        return R.ok().data("userInfo", member);
    }

    @ApiOperation("根据用户id获取用户信息,用于order模块远程调用")
    //根据用户id获取用户信息
    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
        UcenterMember member = ucenterMemberService.getById(id);
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        return ucenterMemberOrder;
    }

    @ApiOperation("查询某一天注册人数")
    @GetMapping("/countRegister/{day}")
    public R countRegister(@PathVariable String day){
        Integer count = ucenterMemberService.countRegister(day);
        return  R.ok().data("countRegister",count);
    }

}

