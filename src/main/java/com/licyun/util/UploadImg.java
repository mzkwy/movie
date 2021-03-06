package com.licyun.util;

import com.licyun.model.User;
import com.licyun.model.Video;
import com.licyun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by 李呈云
 * Description:
 * 2016/10/16.
 */
@Component
public class UploadImg {
    

    public String uploadUserImg(MultipartFile file, User user, String rootPath){
        // 上传目录
        File uploadRootDir = new File(rootPath);
        // 创建文件夹
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        String name = file.getOriginalFilename();
        if(name != ""){
            String imgurl = rootPath +  File.separator + user.getImgUrl();
            File imgFile = new File(imgurl);
            imgFile.delete();
        }
        System.out.println("Client File Name = " + name);
        if (name != null && name.length() > 0) {
            try {
                byte[] bytes = file.getBytes();
                // 创建跨平台的路径
                File serverFile = new File(uploadRootDir.getAbsolutePath()
                        + File.separator + name);
                // 创建输入，输出流
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                System.out.println("Write file: " + serverFile);
            } catch (Exception e) {
                System.out.println("Error Write file: " + name);
            }
        }
        return name;
    }

    public String uploadVideoImg(MultipartFile file, Video video, String rootPath){
        // 上传目录
        File uploadRootDir = new File(rootPath);
        // 创建文件夹
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        String name = file.getOriginalFilename();
        if(name == null || name.isEmpty()){

        }else{
            String imgurl = rootPath +  File.separator + video.getImg();
            File imgFile = new File(imgurl);
            imgFile.delete();
        }
        System.out.println("Client File Name = " + name);
        //上传文件到服务器
        if (name != null && name.length() > 0) {
            try {
                byte[] bytes = file.getBytes();
                // 创建跨平台的路径
                File serverFile = new File(uploadRootDir.getAbsolutePath()
                        + File.separator + name);
                // 创建输入，输出流
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                System.out.println("Write file: " + serverFile);
            } catch (Exception e) {
                System.out.println("Error Write file: " + name);
            }
        }
        //返回上传后的文件名
        return name;
    }
}
