package com.zhy.eduorder.controller;


import com.zhy.commonutils.R;
import com.zhy.eduorder.service.PayLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-22
 */
@RestController
@RequestMapping("/eduorder/paylog")
public class PayLogController {

    @Resource
    private PayLogService payLogService;

    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        Map map = payLogService.createNative(orderNo);
        //返回信息，包含二维码地址，其他信息
        return R.ok().data(map);
    }
    //查询订单支付状态
    //参数：订单号，根据订单号查询 支付状态
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
        Map<String,String> map = payLogService.queryPayStatus(orderNo);
        System.out.println("*****查询订单状态map集合:"+map);
        if(map == null) {
            return R.error().message("支付出错了");
        }
        //如果返回map里面不为空，通过map获取订单状态
        if(map.get("trade_state").equals("SUCCESS")) {//支付成功
            //添加记录到支付表，更新订单表订单状态
            payLogService.updateOrdersStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中");

    }


}

