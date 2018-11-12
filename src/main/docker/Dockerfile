FROM registry.cn-hangzhou.aliyuncs.com/choerodon-tools/javabase:0.7.0
COPY app.jar /config-server.jar
ENTRYPOINT exec java $JAVA_OPTS  -jar /config-server.jar