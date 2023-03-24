package com.zhy.eduservice.service;

import com.zhy.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.eduservice.entity.subject.FirstSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-03-14
 */
public interface EduSubjectService extends IService<EduSubject> {
//添加课程分类
    void saveSubject(MultipartFile file,EduSubjectService eduSubjectService);

    List<FirstSubject> getAllFirstSecondSubject();
}
