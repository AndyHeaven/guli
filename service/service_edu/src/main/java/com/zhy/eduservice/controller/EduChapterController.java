package com.zhy.eduservice.controller;


import com.zhy.commonutils.R;
import com.zhy.eduservice.entity.EduChapter;
import com.zhy.eduservice.entity.chapter.ChapterVo;
import com.zhy.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(description = "章节管理")
@RestController
@RequestMapping("/eduservice/edu-chapter")
//@CrossOrigin
public class EduChapterController {

    @Resource
    private EduChapterService eduChapterService;

    @ApiOperation(value = "根据课程id查询课程大纲列表")
    //课程大纲列表,根据课程id查询
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId) {
        List<ChapterVo> list = eduChapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("allChapterVideo", list);
    }

    @ApiOperation(value = "添加章节")
    //添加章节
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter) {
        eduChapterService.save(eduChapter);
        return R.ok();
    }

    @ApiOperation(value = "查询章节")
    //根据id查询章节
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId) {
        EduChapter chapter = eduChapterService.getById(chapterId);
        return R.ok().data("chapter", chapter);
    }

    @ApiOperation(value = "修改章节")
    //修改章节
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter) {
        eduChapterService.updateById(eduChapter);
        return R.ok();
    }

    @ApiOperation(value = "删除章节")
    //删除章节
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId) {
        boolean flag = eduChapterService.deleteChapter(chapterId);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }

    }

}

