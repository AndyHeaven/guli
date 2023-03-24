package com.zhy.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhy.commonutils.R;
import com.zhy.eduservice.entity.EduCourse;
import com.zhy.eduservice.entity.EduTeacher;
import com.zhy.eduservice.entity.vo.CourseInfoVo;
import com.zhy.eduservice.entity.vo.CoursePublishVo;
import com.zhy.eduservice.entity.vo.CourseQuery;
import com.zhy.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-15
 */
@Api(description = "课程管理")
@RestController
@RequestMapping("/eduservice/edu-course")
//@CrossOrigin
public class EduCourseController {
    @Resource
    private EduCourseService eduCourseService;


    //逻辑删除课程
//    @ApiOperation(value = "逻辑删除课程")
//    @DeleteMapping("{id}")
//    public R removeCourse(
//            @ApiParam(name = "id", value = "课程ID", readOnly = true)
//            @PathVariable String id) {
//        boolean b = eduCourseService.removeById(id);
//        return b ? R.ok() : R.error();
//    }
    //课程列表
    @ApiOperation(value = "多条件组合查询并分页")
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public R pageCourseCondition(
            @PathVariable long current,
            @PathVariable long limit,
            @RequestBody(required = false) CourseQuery courseQuery) {
        //创建page对象
        Page<EduCourse> pageCourse = new Page<>(current, limit);
        //构建条件
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        String begin = courseQuery.getBegin();
        String end = courseQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(title)) {
            //构建条件
            wrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("status", status);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
        //根据创建时间，排序
        wrapper.orderByDesc("gmt_create");
        //调用方法实现条件查询分页
        eduCourseService.page(pageCourse, wrapper);
        long total = pageCourse.getTotal();//总记录数
        List<EduCourse> records = pageCourse.getRecords(); //数据list集合
        return R.ok().data("total", total).data("rows", records);
    }

    //添加课程基本信息的方法
    @ApiOperation(value = "添加课程基本信息")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        String id = eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    @ApiOperation(value = "查询课程基本信息")
    //根据课程id查询课程基本信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId) {
        CourseInfoVo courseInfoVo = eduCourseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo", courseInfoVo);
    }

    @ApiOperation(value = "修改课程基本信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        eduCourseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    @ApiOperation(value = "查询课程确认信息")
    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id) {
        CoursePublishVo coursePublishVo = eduCourseService.publishCourseInfo(id);
        return R.ok().data("publishCourse", coursePublishVo);
    }

    @ApiOperation(value = "修改课程发布状态")
    //课程最终发布
    //修改课程发布状态
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        //课程未发布是Draft，已发布是Normal
        eduCourse.setStatus("Normal");
        //当你update其他值为空时，不会进行覆盖，有值才覆盖
        boolean b = eduCourseService.updateById(eduCourse);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }
    //删除课程
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId) {
        eduCourseService.removeCourse(courseId);
        return R.ok();
    }


}

