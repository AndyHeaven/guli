package com.zhy.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhy.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.eduservice.entity.frontvo.CourseFrontVo;
import com.zhy.eduservice.entity.frontvo.CourseWebVo;
import com.zhy.eduservice.entity.vo.CourseInfoVo;
import com.zhy.eduservice.entity.vo.CoursePublishVo;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-03-15
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo publishCourseInfo(String id);

    void removeCourse(String courseId);

    //1 条件查询带分页查询课程前台
    Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo);

    //根据课程id，编写sql语句查询课程信息
    CourseWebVo getBaseCourseInfo(String courseId);
}
