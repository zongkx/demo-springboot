http://www.minio.org.cn/download.shtml


`docker pull minio/minio`


`
docker run -p 9000:9000 -p 9001:9001 --name minio \
-e "MINIO_ROOT_USER=test" \
-e "MINIO_ROOT_PASSWORD=123456789" \
-v /mydata/minio/data:/data \
-v /mydata/minio/config:/root/.minio \
-d minio/minio server /data --console-address ":9001"
`

`
docker logs minio
`


```xml

        <dependency>
            <groupId>io.minio</groupId>
            <artifactId>minio</artifactId>
            <version>8.3.4</version>
        </dependency>
```


