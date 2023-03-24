package com.zhy.staservice.controller;


import com.zhy.commonutils.R;
import com.zhy.staservice.client.UcenterClient;
import com.zhy.staservice.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-23
 */
@Api(description = "统计管理")
@RestController
@RequestMapping("/staservice/sta")
//@CrossOrigin
public class StatisticsDailyController {
    @Resource
    private StatisticsDailyService statisticsDailyService;

    //统计某一天注册人数
    @PostMapping("registerCount/{day}")
    public R registerCount(@PathVariable String day){
        statisticsDailyService.registerCount(day);
        return R.ok();
    }

    //图表显示，返回两部分数据，日期json数组，数量json数组
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type,@PathVariable String begin,
                      @PathVariable String end) {
        Map<String,Object> map = statisticsDailyService.getShowData(type,begin,end);
        return R.ok().data(map);
    }
}

