package com.zhy.msmservice.controller;

import com.zhy.commonutils.R;
import com.zhy.msmservice.service.MsmService;
import com.zhy.msmservice.utils.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(description = "短信服务")
@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin
public class MsmController {
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private MsmService msmService;

    @ApiOperation("发送短信验证码")
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone) {
        //首先从redis取验证码，如果取得到直接返回，不再重复发送，如果取不到再生成随机值返回
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return R.ok();
        }
        //生成随机值传递给阿里云，再由阿里云转发
        code = RandomUtil.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        boolean isSend = msmService.sendMessage(param, phone);
        if (isSend) {
            //发送成功，将code放入redis中，并且设置有效时间
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("短信发送失败");
        }

    }

}
