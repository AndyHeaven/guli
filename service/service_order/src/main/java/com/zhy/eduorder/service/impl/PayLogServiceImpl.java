package com.zhy.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.auth.WechatPay2Validator;
import com.zhy.eduorder.entity.Order;
import com.zhy.eduorder.entity.PayLog;
import com.zhy.eduorder.mapper.PayLogMapper;
import com.zhy.eduorder.service.OrderService;
import com.zhy.eduorder.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-22
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {
    @Resource
    private OrderService orderService;

    @Override
    public Map createNative(String orderNo) {
        try {
            //根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            Order order = orderService.getOne(wrapper);
            //使用map设置生成二维码需参数(视频的api已经下架了，微信支付api已经从v2 -v3了，所有我决定就不生成二维码了，直接用自己的微信二维码)
            //最终返回数据 的封装
            Map map = new HashMap();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", "1");  //返回二维码操作状态码
            map.put("code_url", "https://edu-zhy.oss-cn-hangzhou.aliyuncs.com/WxNative.jpg");//二维码地址
            return map;
        } catch (Exception e) {
            throw new GuliException(20001, "生成二维码失败");


        }
    }

    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        //手动改成支付成功
        HashMap<String, String> map = new HashMap<>();
        map.put("out_trade_no", orderNo);
        map.put("trade_state","SUCCESS");
        return map;
    }

    @Override
    public void updateOrdersStatus(Map<String, String> map) {
        //从map获取订单号
        String orderNo = map.get("out_trade_no");
        System.out.println(orderNo);
        //根据订单号查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);

        //更新订单表订单状态
        if(order.getStatus().intValue() == 1) { return; }
        order.setStatus(1);//1代表已经支付
        orderService.updateById(order);

        //向支付表添加支付记录
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderNo);  //订单号
        payLog.setPayTime(new Date()); //订单完成时间
        payLog.setPayType(1);//支付类型 1微信
        payLog.setTotalFee(order.getTotalFee());//总金额(分)

        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id")); //流水号
        payLog.setAttr(JSONObject.toJSONString(map));

        baseMapper.insert(payLog);
    }
}
