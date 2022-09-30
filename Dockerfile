FROM openjdk:11

RUN apt install python3
RUN apt update
RUN apt upgrade
RUN apt install -y ffmpeg

RUN pip install --upgrade pip

RUN pip3 install boto3
RUN pip install numpy==1.20.0
RUN pip install scipy==1.7.3
RUN pip install Pillow==7.2.0

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]