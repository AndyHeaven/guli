package com.zhy.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.zhy.commonutils.R;
import com.zhy.servicebase.exceptionhandler.GuliException;
import com.zhy.vod.service.VodService;
import com.zhy.vod.utils.ConstantVodUtis;
import com.zhy.vod.utils.InitVodClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.util.List;

@Api(description = "小节视频管理")
@RestController
@RequestMapping("/eduvod/video")
//@CrossOrigin
public class VodController {
    @Resource
    VodService vodService;

    //上传视频到阿里云
    @ApiOperation(value = "上传视频到阿里云")
    @PostMapping("uploadAlyVideo")
    public R uploadAlyVideo(MultipartFile file) {
        String videoId = vodService.uploadAlyVideo(file);
        return R.ok().data("videoId", videoId);
    }

    @ApiOperation(value = "删除阿里云视频")
    @DeleteMapping("removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable String id) {
        boolean b = vodService.removeAlyVideo(id);
        return b ? R.ok() : R.error();
    }

    @ApiOperation(value = "批量删除阿里云视频")
    @DeleteMapping("deleteBatch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList) {
        boolean b = vodService.deleteBatch(videoIdList);
        return b ? R.ok() : R.error();
    }

    //根据视频id获取视频凭证
    @GetMapping("getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id) {
        try {
            //创建初始化对象
            DefaultAcsClient client =
                    InitVodClient.initVodClient(ConstantVodUtis.ACCESS_KEY_ID, ConstantVodUtis.ACCESS_KEY_SECRET);
            //创建获取凭证request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            //向request设置视频id
            request.setVideoId(id);
            //调用方法得到凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth",playAuth);
        }catch(Exception e) {
            throw new GuliException(20001,"获取凭证失败");
        }
    }




}
