package org.king2.config;

import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.king2.definition.MinioDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：minio属性配置
 *
 * @author 刘梓江
 * @date 2020/6/3  14:49
 */
@Configuration
@SuppressWarnings("all")
public class MinioConfiguration {

    /**
     * 配置MinIO信息定义
     */
    @Bean
    public MinioDefinition minIoDefinition() {
        return new MinioDefinition(
                "http://47.97.195.103:9090/",
                "liuzijiang",
                "liuzijiang",
                "liuzijiang"
        );
    }

    /**
     * 创建minio客户端操作类
     * @return
     */
    @Bean
    @SneakyThrows
    public MinioClient minioClient(MinioDefinition minioDefinition) {
        return new MinioClient(
                minioDefinition.getUrl(),
                minioDefinition.getAccessKey(),
                minioDefinition.getSecretKey()
        );
    }
}