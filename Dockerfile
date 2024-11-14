# 自己本地 mvn clean package打包下，保证target目录下有jar文件，不想在镜像里打包了
# 使用 JRE 17 作为运行时镜像
FROM gcr.io/distroless/java17-debian11
#FROM eclipse-temurin:17-jre-focal
WORKDIR /app

# 从构建阶段复制 jar 文件
COPY ./target/*.jar app.jar

# 暴露应用端口
EXPOSE 8080

# 设置启动命令
ENTRYPOINT ["java", "-jar", "app.jar"] 