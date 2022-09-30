FROM openjdk:11

RUN apt install python3
RUN python3 --version
RUN apt update
RUN apt install -y ffmpeg

RUN apt install python3-pip -y
RUN pip3 install --upgrade pip

RUN pip3 install boto3
RUN pip3 install numpy==1.20.0
RUN pip3 install scipy==1.7.3
RUN pip3 install Pillow

RUN mkdir python
COPY ./python /python

RUN mkdir result
COPY ./result /result

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]




