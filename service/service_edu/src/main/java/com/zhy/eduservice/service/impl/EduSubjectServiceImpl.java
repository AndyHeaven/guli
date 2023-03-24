package com.zhy.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhy.eduservice.entity.EduSubject;
import com.zhy.eduservice.entity.excel.SubjectData;
import com.zhy.eduservice.entity.subject.FirstSubject;
import com.zhy.eduservice.entity.subject.SecondSubject;
import com.zhy.eduservice.listener.SubjectExcelListener;
import com.zhy.eduservice.mapper.EduSubjectMapper;
import com.zhy.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-14
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {

        try {
            //文件输入流
            InputStream inputStream = file.getInputStream();
            //调用方法读取
            EasyExcel.read(inputStream, SubjectData.class, new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //课程分类列表
    @Override
    public List<FirstSubject> getAllFirstSecondSubject() {
        //查所有一级 parentid == 0
        QueryWrapper<EduSubject> wrapperFirst = new QueryWrapper<>();
        wrapperFirst.eq("parent_id", "0");

        //如何在service调用mapper extends ServiceImpl已经autowired导入了basemapper，可以直接调用 第一种方法：
        List<EduSubject> firstSubjectList = baseMapper.selectList(wrapperFirst);

//        第二种方法：
//        this.list(wrapperfirst)

        //查所有二级 parentid != 0
        QueryWrapper<EduSubject> wrapperSecond = new QueryWrapper<>();
        wrapperSecond.ne("parent_id", "0");
        List<EduSubject> secondSubjectList = baseMapper.selectList(wrapperSecond);
        //创建list集合存储最终封装数据
        ArrayList<FirstSubject> finalSubjectList = new ArrayList<>();
        //封装一级
        //所有一级分类集合遍历，得到每一个一级分类对象，获取每一个一级分类对象值放入final
        //把edusubject中值获取出来，放入firstsubject中
        for (EduSubject eduSubject : firstSubjectList) {
            FirstSubject firstSubject = new FirstSubject();
//            firstSubject.setId(eduSubject.getId());
//            firstSubject.setTitle(eduSubject.getTitle());
            BeanUtils.copyProperties(eduSubject, firstSubject);
            finalSubjectList.add(firstSubject);

//            封装二级分类，在一个一级分类封装好后，查询二级分类列表，找出二级分类parentid为一级分类id的所有二级分类，放入新的列表，最终setChildren
            ArrayList<SecondSubject> secondFinalSubjectList = new ArrayList<>();
            for (EduSubject subject : secondSubjectList) {
                if (subject.getParentId().equals(firstSubject.getId())) {
                    SecondSubject secondSubject = new SecondSubject();
                    BeanUtils.copyProperties(subject, secondSubject);
                    secondFinalSubjectList.add(secondSubject);
                }
            }
            firstSubject.setChildren(secondFinalSubjectList);
        }

        return finalSubjectList;
    }
}
