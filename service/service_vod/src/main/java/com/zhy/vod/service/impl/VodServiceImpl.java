package com.zhy.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.zhy.servicebase.exceptionhandler.GuliException;
import com.zhy.vod.service.VodService;
import com.zhy.vod.utils.ConstantVodUtis;
import com.zhy.vod.utils.InitVodClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {
    /**
     * 流式上传接口
     */
    @Override
    public String uploadAlyVideo(MultipartFile file) {
        /**
         * @param accessKeyId
         * @param accessKeySecret
         * @param title 上传后显示名称
         * @param fileName 上传文件原始名称
         * @param inputStream 上传文件输入流
         */

        String fileName = file.getOriginalFilename();
        String title = fileName.substring(0, fileName.lastIndexOf("."));
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtis.ACCESS_KEY_ID, ConstantVodUtis.ACCESS_KEY_SECRET, title, fileName, inputStream);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);

        String videoId = null;
        if (response.isSuccess()) {
            videoId = response.getVideoId();
        } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            videoId = response.getVideoId();
        }
        return videoId;
    }

    @Override
    public boolean removeAlyVideo(String id) {
        try {
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtis.ACCESS_KEY_ID, ConstantVodUtis.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request设置视频id
            request.setVideoIds(id);
            //调用初始化对象方法实现删除
            client.getAcsResponse(request);
            return true;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001, "删除视频失败！");
        }
    }

    @Override
    public boolean deleteBatch(List<String> videoIdList) {
        try {
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtis.ACCESS_KEY_ID, ConstantVodUtis.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //把videoList值转换成1，2，3
            String join = StringUtils.join(videoIdList.toArray(), ",");
            //向request设置视频id
            request.setVideoIds(join);
            //调用初始化对象方法实现删除
            client.getAcsResponse(request);
            return true;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001, "删除视频失败！");
        }
    }
}
