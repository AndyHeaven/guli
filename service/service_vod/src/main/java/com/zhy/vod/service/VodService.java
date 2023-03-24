package com.zhy.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    String uploadAlyVideo(MultipartFile file);

    boolean removeAlyVideo(String id);

    boolean deleteBatch(List<String> videoIdList);
}
