package com.zhy.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhy.eduservice.entity.EduSubject;
import com.zhy.eduservice.entity.excel.SubjectData;
import com.zhy.eduservice.service.EduSubjectService;
import com.zhy.servicebase.exceptionhandler.GuliException;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {


    //因为这个监听类不能交给spring管理，需要自己new，不能注入其他对象，不能实现数据库管理
    //通过有参构造器把subjectService传递过来
    public EduSubjectService subjectService;

    public SubjectExcelListener() {
    }

    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }


    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData == null) {
            throw new GuliException(20001, "文件数据为空");
        }
        //一行一行读取，第一个值是一级分类，第二个值是二级分类
        EduSubject existFirstSubject = this.existFirstSubject(subjectService, subjectData.getFirstSubjectName());
        if (existFirstSubject == null) {//如果没有相同一级分类，进行添加
            existFirstSubject = new EduSubject();
            existFirstSubject.setParentId("0");
            existFirstSubject.setTitle(subjectData.getFirstSubjectName());
            subjectService.save(existFirstSubject);
        }

        //添加二级分类
        //判断耳机分类是否重复
        String pid = existFirstSubject.getId();
        EduSubject existSecondSubject = this.existSecondSubject(subjectService, subjectData.getSecondSubjectName(), pid);
        if (existSecondSubject == null) {//如果没有相同一级分类，进行添加
            existSecondSubject = new EduSubject();
            existSecondSubject.setParentId(pid);
            existSecondSubject.setTitle(subjectData.getSecondSubjectName());
            subjectService.save(existSecondSubject);
        }
    }

    //判断一级分类不能重复添加
    private EduSubject existFirstSubject(EduSubjectService subjectService, String name) {
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", name);
        queryWrapper.eq("parent_id", "0");
        EduSubject subject = subjectService.getOne(queryWrapper);
        return subject;
    }

    //判断二级分类不能重复添加
    private EduSubject existSecondSubject(EduSubjectService subjectService, String name, String pid) {
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", name);
        queryWrapper.eq("parent_id", pid);
        EduSubject subject = subjectService.getOne(queryWrapper);
        return subject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
