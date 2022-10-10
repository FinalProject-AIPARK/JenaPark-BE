FROM openjdk:11

RUN apt install python3
RUN apt update

RUN apt install python3-pip -y

RUN apt install locales - y
RUN apt install -y \
    language-pack-ko && \
    dpkg-reconfigure locales && \
    locale-gen ko_KR.UTF-8 && \
    /usr/sbin/update-locale LANG=ko_KR.UTF-8

# 한글을 출력하기 위해 환경변수 등록
ENV LANG=ko_KR.UTF-8
ENV LANGUAGE=ko_KR.UTF-8
ENV LC_ALL=ko_KR.UTF-8

# 파이썬에서 한글을 사용할 수 있도록 환경변수 등록
ENV PYTHONIOENCODING=UTF-8

RUN pip3 install --upgrade pip

RUN pip3 install boto3
RUN pip3 install numpy==1.20.0
RUN pip3 install scipy==1.7.3
RUN pip3 install Pillow
RUN pip3 install gTTS
RUN pip3 install moviepy

RUN mkdir python
COPY ./python /python

RUN mkdir result
COPY ./result /result

ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]




