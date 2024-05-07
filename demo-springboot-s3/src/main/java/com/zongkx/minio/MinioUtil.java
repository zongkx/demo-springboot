package com.zongkx.minio;

import com.google.common.io.ByteStreams;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zongkxc
 */
@Component
@Slf4j
public class MinioUtil {

    private static MinioClient minioClient;

    public MinioUtil(MinioConf minioConf) {
        minioClient =
                minioConf.getMinioClient();
    }

    /**
     * 检查该租户是否已存在
     */
    public static boolean bucketExists(String bucketId) throws S3Exception {
        boolean bucketExists;
        try {
            bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketId).build());
        } catch (Exception e) {
            throw new S3Exception(e);
        }
        return bucketExists;
    }

    /**
     * 检查对象是否存在
     */
    public static boolean objectExists(String bucketId, String key) {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketId)
                        .prefix(key)
                        .build());
        return results.iterator().hasNext();
    }

    /**
     * 获取对象内容
     */
    public static String getObjectContent(String bucketId, String key) throws S3Exception {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucketId).object(key)
                .build();
        try (InputStream inputStream = minioClient.getObject(getObjectArgs)) {
            return new String(ByteStreams.toByteArray(inputStream));
        } catch (Exception e) {
            throw new S3Exception(e);
        }
    }

    /**
     * 创建桶
     */
    public static void createBucketIfNotExists(String bucketId) throws S3Exception {
        if (!bucketExists(bucketId)) {
            try {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketId).build());
            } catch (Exception e) {
                throw new S3Exception(e);
            }
        }
    }


    /**
     * 删除桶内某个文件夹
     */
    public static void removeFolder(String bucketId, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketId)
                        .prefix(key)
                        .build());
        if (results.iterator().hasNext()) {
            List<DeleteObject> objects = new LinkedList<>();
            for (Result<Item> result : results) {
                objects.add(new DeleteObject(result.get().objectName()));
            }
            Iterable<Result<DeleteError>> results1 = minioClient.removeObjects(
                    RemoveObjectsArgs.builder().bucket(bucketId).objects(objects).build());
            for (Result<DeleteError> result : results1) {
                DeleteError error = result.get();
                log.error("Error in deleting object " + error.objectName() + "; " + error.message());
            }
        }
    }

    /**
     * 添加对象
     */
    public static void putObject(String bucketId, String key, String content) throws S3Exception {
        createBucketIfNotExists(bucketId);
        try (InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketId).object(key)
                            .stream(inputStream, inputStream.available(), -1)
                            .build());
        } catch (Exception e) {
            throw new S3Exception(e);
        }
    }

    /**
     * 删除某个对象
     */
    public static void removeObject(String bucketId, String key) throws S3Exception {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketId)
                            .object(key)
                            .build());
        } catch (Exception e) {
            throw new S3Exception( e);
        }

    }
    /**
     * 获取对象内容
     *
     */
    public static List<String> getObjects(String bucketId) throws S3Exception {
        List<String> resultList;
        try {
            ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
                    .bucket(bucketId).build();
            Iterable<Result<Item>> listObjects = minioClient.listObjects(listObjectsArgs);
            resultList = new ArrayList<>();
            if (listObjects.iterator().hasNext()) {
                for (Result<Item> result : listObjects) {
                    resultList.add(result.get().objectName());
                }
            }
        } catch (Exception e) {
            throw new S3Exception(e);
        }
        return resultList;
    }
}
