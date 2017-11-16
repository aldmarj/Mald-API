FROM tomcat:8.0-jre8-alpine
RUN rm -rf /usr/local/tomcat/webapps/*
COPY /target/API.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]