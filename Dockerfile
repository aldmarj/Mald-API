FROM tomcat:8.0-jre8-alpine
CMD ["rm", "-rf", "/usr/local/tomcat/webapps/ROOT/"]
COPY /target/API.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]