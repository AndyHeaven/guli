package com.zhy.eduservice.client;

import com.zhy.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)//注册中心被调用的服务名,调用hystrix熔断机制，出错后执行哪里的方法
public interface VodClient {
    //定义调用方法路径,PathVariable一定要指定一个名称,否则会出错
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable("id") String id);

    @DeleteMapping("/eduvod/video/deleteBatch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
