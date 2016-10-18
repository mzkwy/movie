package com.licyun.service;

import com.licyun.model.Video;

import java.util.List;

/**
 * Created by 李呈云
 * Description:
 * 2016/10/17.
 */
public interface VideoService {

    Video findById(int id);

    Video findByName(String name);

    List<Video> findVideosByPlayType(String type);

    List<Video> findAllVideos();

    Long insertVideo(Video video);

    Long updateVideo(Video video);

    Long deleteVideoById(int id, String rootPath);
}