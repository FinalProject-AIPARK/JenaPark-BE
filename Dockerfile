FROM openjdk:11

RUN apt install python3
RUN apt update
RUN apt install -y ffmpeg

RUN pip3 install --upgrade pip3

RUN pip3 install boto3
RUN pip3 install numpy==1.20.0
RUN pip3 install scipy==1.7.3
RUN pip3 install Pillow==7.2.0

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]