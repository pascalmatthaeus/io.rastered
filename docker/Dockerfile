FROM alpine:latest
WORKDIR /
RUN apk update
RUN apk upgrade
RUN apk add git
RUN git clone https://github.com/pascalmatthaeus/io.rastered
RUN apk add openjdk17-jre-headless
RUN mkdir /tomcat
WORKDIR /tomcat
RUN wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.0.21/bin/apache-tomcat-10.0.21.tar.gz \
    && tar -xzf apache-tomcat-10.0.21.tar.gz \
    && rm apache-tomcat-10.0.21.tar.gz
WORKDIR /tomcat/apache-tomcat-10.0.21
RUN rm -rf webapps/ROOT
RUN cp /io.rastered/rastered.io/app/target/app-0.1.war \
    webapps/ROOT.war
RUN chmod +x bin/*.sh

RUN mkdir -p /home/pascal/Pictures/
ADD lenna.bmp /home/pascal/Pictures/lenna.bmp

WORKDIR /io.rastered/rastered-frontend
RUN apk add nodejs && apk add npm && npm install

EXPOSE 8080
EXPOSE 3000

ENTRYPOINT /tomcat/apache-tomcat-10.0.21/bin/catalina.sh run
