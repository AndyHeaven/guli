package com.zhy.eduservice.controller;


import com.zhy.commonutils.R;
import com.zhy.eduservice.client.VodClient;
import com.zhy.eduservice.entity.EduChapter;
import com.zhy.eduservice.entity.EduVideo;
import com.zhy.eduservice.service.EduVideoService;
import com.zhy.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-15
 */
@Api(description = "小节管理")
@RestController
@RequestMapping("/eduservice/edu-video")
//@CrossOrigin
public class EduVideoController {
    @Resource
    private EduVideoService eduVideoService;
    @Resource
    //注入VodClient
    private VodClient vodClient;
    @ApiOperation(value = "添加小节")
    @PostMapping("addVideo")
    //添加小节
    public R addVideo(@RequestBody EduVideo eduVideo) {
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    @ApiOperation(value = "查询小节")
    //根据id查询章节
    @GetMapping("getVideoInfo/{videoId}")
    public R getVideoInfo(@PathVariable String videoId) {
        EduVideo video = eduVideoService.getById(videoId);
        return R.ok().data("video", video);
    }

    //删除小节的同时删除视频，通过微服务调用，调用service_vod中的方法删除视频
    @ApiOperation(value = "删除小节 and 视频")
    //删除小节
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id) {
        //根据小节id得到视频id，在调用方法删除视频
        EduVideo video = eduVideoService.getById(id);
        String videoSourceId = video.getVideoSourceId();
        if (!StringUtils.isEmpty(videoSourceId)){
            R r = vodClient.removeAlyVideo(videoSourceId);
            if (r.getCode() == 20001){
                throw new GuliException(20001,"删除视频失败，熔断器...");
            }
        }
        boolean b = eduVideoService.removeById(id);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "修改小节")
    @PostMapping("updateVideo")
    //修改小节
    public R updateVideo(@RequestBody EduVideo eduVideo) {
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }


}

