FROM tomcat:9-jre8

RUN apt-get update
RUN apt-get install -y openjdk-8-jdk openjdk-8-jdk-headless

WORKDIR /interpres
COPY . .
RUN javac -classpath .:/usr/local/tomcat/lib/servlet-api.jar -d WEB-INF/classes src/interpres/main/Interpres.java
RUN mkdir --parents /usr/local/tomcat/webapps/interpres
RUN cp --recursive WEB-INF /usr/local/tomcat/webapps/interpres/

CMD ["catalina.sh", "run"]
