package org.king2.definition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：minio属性定义类
 *
 * @author 刘梓江
 * @date 2020/6/3  14:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
public class MinioDefinition {

    /**
     * 地址
     */
    private String url;

    /**
     * 账号
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;

    /**
     * 默认桶名称
     */
    private String defaultBucketName;
}
