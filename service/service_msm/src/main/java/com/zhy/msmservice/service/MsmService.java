package com.zhy.msmservice.service;

import java.util.Map;

public interface MsmService {
    boolean sendMessage(Map<String, Object> param, String phone);
}
