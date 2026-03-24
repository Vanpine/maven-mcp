# 基础镜像：OpenJDK 21 精简版（Debian 系，体积小、性能优）
FROM openjdk:21-jdk-slim


# 设置时区为中国上海（避免日志时间差 8 小时）
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建工作目录
WORKDIR /app

# 复制 mcp-client 模块下的 jar 包到容器内
# 注意：请将下面的 jar 包名替换为你实际打包后的名称
COPY alibaba-ai-test-1.0-SNAPSHOT.jar app.jar

# 暴露应用端口（根据你的 application.yml 配置修改，比如 8080）
EXPOSE 8080

# 启动 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "/app/app.jar"]