package com.zhy.oss.controller;

import com.zhy.commonutils.R;
import com.zhy.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

//交由spring管理返回json数据
@RestController
@RequestMapping("eduoss/fileoss")
@Api(description = "Oss操作")
//@CrossOrigin
public class OssController {
    @Resource
    private OssService ossService;

    @ApiOperation(value = "上传文件至oss")
    @PostMapping
    public R uploadOssFile(MultipartFile file) {
        //获取上传文件
        String url = ossService.uploadFileAvatar(file);

        return R.ok().data("url", url);
    }
}
