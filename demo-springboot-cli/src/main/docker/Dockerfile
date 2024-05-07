FROM registry.cn-qingdao.aliyuncs.com/zongkx/jdk8
VOLUME /tmp
ADD demo-springboot-cli-1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=GMT+08","-jar","/app.jar"]
