package com.zhy.eduservice.service;

import com.zhy.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-03-15
 */
public interface EduChapterService extends IService<EduChapter> {


    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    boolean deleteChapter(String chapterId);

    void removeChapterByCourseId(String courseId);
}
