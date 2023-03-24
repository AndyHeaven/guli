package com.zhy.eduservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
//为了读取excel而创建对应的实体类
public class SubjectData {

    @ExcelProperty(index = 0)
    private String firstSubjectName;

    @ExcelProperty(index = 1)
    private String secondSubjectName;


}
