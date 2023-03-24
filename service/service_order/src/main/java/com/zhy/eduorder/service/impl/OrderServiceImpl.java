package com.zhy.eduorder.service.impl;

import com.zhy.commonutils.ordervo.CourseWebVoOrder;
import com.zhy.commonutils.ordervo.UcenterMemberOrder;
import com.zhy.eduorder.client.EduClient;
import com.zhy.eduorder.client.UcenterClient;
import com.zhy.eduorder.entity.Order;
import com.zhy.eduorder.mapper.OrderMapper;
import com.zhy.eduorder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.eduorder.utils.OrderNoUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-22
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Resource
    private EduClient eduClient;
    @Resource
    private UcenterClient ucenterClient;

    @Override
    public String createOrders(String courseId, String memberIdByJwtToken) {
        //远程调用根据课程id获取课程信息
        CourseWebVoOrder courseWebVoOrder = eduClient.getCourseInfoOrder(courseId);
        //通过远程调用根据id获取用户信息
        UcenterMemberOrder ucenterMemberOrder = ucenterClient.getUserInfoOrder(memberIdByJwtToken);
        Order order = new Order();
        String orderNo = OrderNoUtil.getOrderNo();
        order.setOrderNo(orderNo);//订单号
        order.setCourseId(courseId);//课程id
        order.setCourseTitle(courseWebVoOrder.getTitle());
        order.setCourseCover(courseWebVoOrder.getCover());
        order.setTeacherName(courseWebVoOrder.getTeacherName());
        order.setTotalFee(courseWebVoOrder.getPrice());
        order.setMemberId(memberIdByJwtToken);
        order.setMobile(ucenterMemberOrder.getMobile());
        order.setNickname(ucenterMemberOrder.getNickname());

        order.setStatus(0);//订单状态 0-未支付 1-已支付
        order.setPayType(1);//支付类型 1-支付宝 2-微信
        baseMapper.insert(order);

        return orderNo;
    }
}
