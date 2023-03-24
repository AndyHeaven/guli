package com.zhy.eduorder.client;

import com.zhy.commonutils.ordervo.CourseWebVoOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name = "service-edu")//注册中心被调用的服务名,调用hystrix熔断机制，出错后执行哪里的方法
public interface EduClient {
    //远程调用，注解pathvariable必须要加参数，路径名称要写全的
    @PostMapping("/eduservice/coursefront/getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable("id") String id);
}
