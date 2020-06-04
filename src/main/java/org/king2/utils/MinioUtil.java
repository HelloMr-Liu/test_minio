package org.king2.utils;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import io.minio.policy.PolicyType;
import lombok.SneakyThrows;
import org.king2.definition.MinioDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * 描述：minio文件上传下载工具类
 *
 * @author 刘梓江
 * @date 2020/6/3  14:54
 */
@Component
@SuppressWarnings("all")
public class MinioUtil {

    /**
     * 注入：操作minio信息客户端工具类
     */
    @Autowired
    private MinioClient  minioClient;


    /**
     * 注入：操作minio连接属性信息
     */
    @Autowired
    private MinioDefinition minioDefinition;

    /**
     * 文件上传
     * @param multipartFile         文件对象
     * @param fileName              文件名称
     * @param bucketName            桶名称
     * @return
     * @throws Exception
     */
    @SneakyThrows
    public Object uploadFile(MultipartFile multipartFile,String fileName,String bucketName) {
        //获取当前桶名称
        String currentBucketName= StringUtils.isEmpty(bucketName)?minioDefinition.getDefaultBucketName():bucketName;

        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(currentBucketName);

        // 不存在就创建
        if (!isExist) {
            minioClient.makeBucket(currentBucketName);
            //创建捅策略(" *  代表任何人都可以在线读写文件信息,注意：4.0后就貌似不能设置桶策略了")
            minioClient.setBucketPolicy(currentBucketName,"*", PolicyType.READ_WRITE);
            //minioClient.setBucketPolicy(currentBucketName,"KT", PolicyType.READ_WRITE);
        }
        // 上传文件
        minioClient.putObject(minioDefinition.getDefaultBucketName(), fileName,multipartFile.getInputStream(), multipartFile.getContentType());

        // 上传成功查询图片地址
        String url = minioClient.getObjectUrl(minioDefinition.getDefaultBucketName(), fileName);

        return url;
    }

    /**
     * 文件上传
     * @param multipartFile         文件对象
     * @param fileName              文件名称
     * @return
     * @throws Exception
     */
    @SneakyThrows
    public Object uploadFile(MultipartFile multipartFile,String fileName) throws Exception {
        return uploadFile(multipartFile, fileName, null);
    }



    /**
     * 创建一个新的文件名称
     * @param file
     * @return
     */
    public static String createName(MultipartFile file) {
        StringBuffer fileName = new StringBuffer();
        String fileNameFix =DigestUtils.md5DigestAsHex((UUID.randomUUID().toString()+System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
        String originalFilename = file.getOriginalFilename();
        String fileNameEnd = originalFilename.substring(originalFilename.lastIndexOf("."));
        fileName.append(fileNameFix).append(fileNameEnd);
        return fileName.toString();
    }

    /**
     * 删除对应的文件
     * @param bucketName       桶名称
     * @param delFileName      指定删除的文件名称
     * @return
     * @throws Exception
     */
    @SneakyThrows
    public Object delFile(String bucketName,String delFileName){
        //获取对应的桶名称
        String backutName=StringUtils.isEmpty(bucketName)?minioDefinition.getDefaultBucketName():bucketName;
        minioClient.removeObject(minioDefinition.getDefaultBucketName(), delFileName.substring(delFileName.lastIndexOf("/") + 1));
        return new Object();
    }
    /**
     * 删除对应的文件
     * @param delFileName      指定删除的文件名称
     * @return
     * @throws Exception
     */
    @SneakyThrows
    public Object delFile(String delFileName){
        return delFile(null,delFileName);
    }





    /**
     * 创建捅信息
     * @param bucketName   捅名称
     * @return
     */
    @SneakyThrows
    public Object createBucketName(String bucketName){
        ///判断捅是否存在
        if(!minioClient.bucketExists(bucketName)){
            minioClient.makeBucket(bucketName);
        }
        return new Object();
    }

    /**
     * 删除捅下的所有文件信息
     * @param bucketName      捅名称
     * @return
     * @throws Exception
     */
    @SneakyThrows
    public Object delFileByBucket(String bucketName){
        //获取捅下的所有文件信息
        Iterable<Result<Item>> myObjects = minioClient.listObjects(bucketName);
        List<String> delObjectNames = new LinkedList<String>();
        if(myObjects!= null && myObjects.iterator().hasNext()){
            for (Result<Item> result : myObjects) {
                //获取当前文件对象
                Item item = result.get();
                //System.out.println(item.lastModified() + ", " + item.size() + ", " + item.objectName());
                minioClient.removeObject(bucketName,item.objectName());
            }
            //先删除对应捅下的所有文件信息(目前不知道为什么这种不行)
            //minioClient.removeObject(bucketName,delObjectNames);
        }
        return new Object();
    }

    /**
     * 删除捅信息
     * 提示：就算捅中有文件信息也会被删除掉
     * @param bucketName   捅名称
     * @return
     */
    @SneakyThrows
    public Object delBucketName(String bucketName){
        ///判断捅是否存在(注意：这种方式只能删除空桶)
        if(minioClient.bucketExists(bucketName)){
            //删除捅操作
            minioClient.removeBucket(bucketName);
        }
        return new Object();
    }
}
