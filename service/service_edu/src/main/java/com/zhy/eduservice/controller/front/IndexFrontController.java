package com.zhy.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhy.commonutils.R;
import com.zhy.eduservice.entity.EduCourse;
import com.zhy.eduservice.entity.EduTeacher;
import com.zhy.eduservice.service.EduCourseService;
import com.zhy.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
@Api(description = "前台")
public class IndexFrontController {
    @Resource
    private EduCourseService courseService;
    @Resource
    private EduTeacherService teacherService;

    //查询前8条热门课程，查询前4条名师
    //还可以继续优化，课程和讲师列表也跟banner一样用redis缓存@Cacheable，不过这个时候要把下面这两个方法拆分，并放在service层操作。增删改查也对应的也要清空缓存@CacheEvict
    @GetMapping("index")
    public R index() {
        //查询前8条热门课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        List<EduCourse> eduList = courseService.list(wrapper);

        //查询前4条名师
        QueryWrapper<EduTeacher> wrapperTeacher = new QueryWrapper<>();
        wrapperTeacher.orderByDesc("id");
        wrapperTeacher.last("limit 4");
        List<EduTeacher> teacherList = teacherService.list(wrapperTeacher);

        return R.ok().data("eduList",eduList).data("teacherList",teacherList);
    }
}
