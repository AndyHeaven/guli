package com.zhy.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhy.eduservice.entity.EduChapter;
import com.zhy.eduservice.entity.EduVideo;
import com.zhy.eduservice.entity.chapter.ChapterVo;
import com.zhy.eduservice.entity.chapter.VideoVo;
import com.zhy.eduservice.mapper.EduChapterMapper;
import com.zhy.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.eduservice.service.EduVideoService;
import com.zhy.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-15
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Resource
    private EduVideoService eduVideoService;

    //课程大纲列表,根据课程id查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id", courseId);
        List<EduChapter> chapterList = baseMapper.selectList(wrapperChapter);

        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id", courseId);
        //当前basemapper是EduChapter的，video无法使用，所以引入eduvideoservice
        List<EduVideo> videoList = eduVideoService.list(wrapperVideo);

        //最终表
        List<ChapterVo> finalList = new ArrayList<>();

        for (EduChapter chapter : chapterList) {
            //循环遍历将章节放入
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);

            finalList.add(chapterVo);

            List<VideoVo> finalVideoList = new ArrayList<>();
            //将小节遍历放入
            for (EduVideo video : videoList) {
                if (chapter.getId().equals(video.getChapterId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video, videoVo);
                    finalVideoList.add(videoVo);
                }
            }
            chapterVo.setChildren(finalVideoList);
        }
        return finalList;
    }

    //删除章节，如果章节下面有小节，不让删除，如果没有，让删除
    @Override
    public boolean deleteChapter(String chapterId) {
        //根据章节id查询是否有小节
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id", chapterId);
        int count = eduVideoService.count(videoQueryWrapper);
        if (count == 0) {
            boolean b = this.removeById(chapterId);
            return b;
        } else {
            throw new GuliException(20001, "不能删除");
        }
    }

    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
