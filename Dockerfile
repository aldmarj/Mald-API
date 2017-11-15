FROM tomcat:8.0-jre8-alpine
COPY /target/API.war /usr/local/tomcat/webapps/api.war
CMD ["catalina.sh", "run"]