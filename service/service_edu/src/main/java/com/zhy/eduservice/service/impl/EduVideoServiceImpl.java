package com.zhy.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhy.eduservice.client.VodClient;
import com.zhy.eduservice.entity.EduVideo;
import com.zhy.eduservice.mapper.EduVideoMapper;
import com.zhy.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-15
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Resource
    private VodClient vodClient;

    //TODO 删除小节并删除对应文件
    @Override
    public void removeVideoByCourseId(String courseId) {
        // 1、根据课程id查询课程中所有视频id，删除视频
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id", courseId);
        videoWrapper.select("video_source_id");
        List<EduVideo> videoList = baseMapper.selectList(videoWrapper);
        // List<EduVideo>变成List<String>，因为deleteBatch传入的是一个字符串
        List<String> videoIds = new ArrayList<>();
        for (EduVideo eduVideo : videoList) {
            if (!StringUtils.isEmpty(eduVideo.getVideoSourceId()))
                videoIds.add(eduVideo.getVideoSourceId());
        }
        if (videoIds.size() > 0) {
            vodClient.deleteBatch(videoIds);
        }

        //删除小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);
    }
}
