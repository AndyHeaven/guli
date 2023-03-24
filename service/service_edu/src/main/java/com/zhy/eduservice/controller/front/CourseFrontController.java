package com.zhy.eduservice.controller.front;

import com.zhy.commonutils.R;
import com.zhy.commonutils.ordervo.CourseWebVoOrder;
import com.zhy.eduservice.entity.EduCourse;
import com.zhy.eduservice.entity.chapter.ChapterVo;
import com.zhy.eduservice.entity.frontvo.CourseFrontVo;
import com.zhy.eduservice.entity.frontvo.CourseWebVo;
import com.zhy.eduservice.service.EduChapterService;
import com.zhy.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Api(description = "前台课程")
@RestController
@RequestMapping("/eduservice/coursefront")
@CrossOrigin
public class CourseFrontController {

    @Resource
    private EduCourseService courseService;

    @Resource
    private EduChapterService chapterService;

    @ApiOperation("条件查询带分页查询课程")
    //1 条件查询带分页查询课程
    @PostMapping("getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page, @PathVariable long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo) {
        Page<EduCourse> pageCourse = new Page<>(page,limit);
        Map<String,Object> map = courseService.getCourseFrontList(pageCourse,courseFrontVo);
        //返回分页所有数据
        return R.ok().data(map);
    }

    @ApiOperation("课程详情的方法")
    //2 课程详情的方法
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId) {
        //根据课程id，编写sql语句查询课程信息
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);

        //根据课程id查询章节和小节
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);

        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList);
    }

    @ApiOperation("根据课程id查询课程信息,用于order模块，远程调用")
    @PostMapping("getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id){
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(id);
        BeanUtils.copyProperties(courseWebVo,courseWebVoOrder);
        return courseWebVoOrder;
    }
}












