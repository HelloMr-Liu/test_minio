package org.king2.controller;

import lombok.SneakyThrows;
import org.king2.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 描述：当前类说说明
 *
 * @author 刘梓江
 * @date 2020/6/3  17:19
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/file")
public class FileController {


    @Autowired
    private MinioUtil minioUtil;


    @GetMapping("/upload")
    @SneakyThrows
    public Object upload(MultipartFile file){
        //获取新的文件名称
        String newFileName=minioUtil.createName(file);
        Object o = minioUtil.uploadFile(file, newFileName);
        return o.toString();
    }

    @GetMapping("/deleteBucket")
    @SneakyThrows
    public String deleteBucket(String bucketName){
        return minioUtil.delBucketName(bucketName).toString();
    }

    @GetMapping("/delFileByBucket")
    @SneakyThrows
    public String delFileByBucket(String bucketName){
         return minioUtil.delFileByBucket(bucketName).toString();
    }


    @GetMapping("/deleteFile")
    @SneakyThrows
    public Object deleteFile(String fileName){
        return minioUtil.delFile(fileName).toString();
    }
}
