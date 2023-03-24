package com.zhy.eduservice.controller;


import com.zhy.commonutils.R;
import com.zhy.eduservice.entity.subject.FirstSubject;
import com.zhy.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-14
 */
@Api(description = "课程分类管理")
@RestController
@RequestMapping("/eduservice/edu-subject")
//@CrossOrigin
public class EduSubjectController {
    @Resource
    private EduSubjectService eduSubjectService;
    @ApiOperation(value = "添加课程分类")
    //添加课程分类
    //获取上传过来的文件，把文件内容读取出来
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file) {
        eduSubjectService.saveSubject(file, eduSubjectService);
        return R.ok();
    }
    @ApiOperation(value = "获取所有课程分类")
    @GetMapping("getAllSubject")
    public R getAllSubject() {
        List<FirstSubject> list = eduSubjectService.getAllFirstSecondSubject();
        return R.ok().data("list", list);
    }
}

