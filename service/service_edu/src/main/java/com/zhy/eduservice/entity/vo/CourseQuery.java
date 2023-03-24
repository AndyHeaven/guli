package com.zhy.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//用于和前端传递课程查询信息的传递对象，和TeacherQuery类似
@Data
public class CourseQuery {
    @ApiModelProperty("课程名称，模糊查询")
    private String title;
    @ApiModelProperty("课程发布状态 Draft未发布 Normal已发布")
    private String status;
    @ApiModelProperty(value = "查询开始时间",example = "2019-01-01 10：10：10")
    private String begin;
    @ApiModelProperty(value = "查询结束时间",example = "2019-12-01 10：10：10")
    private String end;
}
