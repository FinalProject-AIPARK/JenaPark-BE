
# 가상영상합성 서비스 페이지 개선

> 프로젝트 기간 <br>
`2022.09.06 - 2022.10.14`

> 프로젝트 목적 <br>

- 다수의 팀원과의 협업능력 향상
- 기업이 원하는 요구사항을 분석하고 우선순위를 정하여 개발하는 법 습득하기
- 칸반보드를 통해 프로젝트 관리하는 법 익히기

> 프로젝트 팀원 👪

- **BackEnd**
  - 김윤겸([@yoongyum](https://github.com/yoongyum)), 이창희([@WindowH22](https://github.com/WindowH22)), 강소영([@soyoungkangme](https://github.com/soyoungkangme))
- **FrontEnd**
  - 김민구, 김수현, 김채욱, 신재일
- **UIUX**
  - 김유찬, 김동관, 이승현

## Devops

![image](https://user-images.githubusercontent.com/72365815/195251298-5ae27df9-fb05-4717-9539-fe9dcbb403c3.png)

- 두개의 EC2를 사용, 하나는 CICD용 젠킨스 서버, 다른 하나는 API Server 

- Redis 컨테이너는 동시접근을 제어하기위해 AccessToken과 토튼 재발급을 위한 RefreshToken을 저장하는 용도로 사용, <br>
또한 로그아웃 시 해당 AccessToken을 블랙리스트로 추가해서 접근을 제어

- 파이썬 컨테이너를 따로 생성하지 않고, Spring 서버에서 파이썬 스크립트를 사용하여 음성파일과, 영상파일을 생성한뒤 S3에 저장하도록 구현 <br>
Async를 사용하여, 음성생성과 영상생성 로직을 비동기 처리


![alb2](https://user-images.githubusercontent.com/72365815/195304987-6f399abd-ed8d-4556-b7cb-f89893587f16.png)
- ACM을 사용하여 SSL인증서 발급 후, ALB에 세팅
- Client에서 HTTPS로 요청하는 것을 ALB의 Listener가 가로챈 후 API Server로 요청을 우회시켜줌

![s33](https://user-images.githubusercontent.com/72365815/195304591-16e3dea7-7ffa-4e9b-a527-6233537a10c9.png)
- CloudFront를 S3에 연결 후, Client는 CloudFront를 통해서 S3의 미디어데이터를 다운받음

## Description

```
AI파크는 인공지능&음성 생성 기술을 활용한 다국어 디지털 휴먼 제작 솔루션을 제공합니다.
이번 기업연계 프로젝트는 AI파크의 서비스 페이지 개선을 목표로 하는 프로젝트입니다.
(기업에서 IA, 와이어프레임, API를 제공받았습니다.)

텍스트를 입력하고 성별과 여러가지 언어의 음성을 선택하고 속도와 톤 조절을 한 후에
아바타를 선택하고 의상과 배경을 조합하여 가상 영상을 생성합니다.
```
![jenaParkThumbnail](https://user-images.githubusercontent.com/72365815/197697248-2b3e7643-fad7-40d8-834a-7788e25e696c.gif)

<br>

## Env

> ### FrontEnd - [Repo](https://github.com/FinalProject-AIPARK/JenaPark-FE)
`React`, `Redux`, `Vite`, `TypeScript` <br><br>


> ### BackEnd
`Java11`, `Spring Data JPA`, `Spring Security`, `JWT`, `Redis`, `MySQL`, `Swagger`, `OAuth2` <br>

`Python3`, `gTTS`, `boto3`, `moviepy`, `scipy`, `numpy`

#### OpenAPI
`Google Login`, `Kakao Login`




## DB Diagram
![image](https://user-images.githubusercontent.com/72365815/195251816-4f673977-1e3a-4ecc-b0b5-da4040172b80.png)

<br>

## Communication

Discord를 주로 사용하여 UIUX/FE/BE 팀원들과 실시간으로 소통을 원활하게 이루었습니다.
![image](https://user-images.githubusercontent.com/72365815/195583261-bafc3c4d-85d7-4882-bf23-409a89b17e3b.png)


> ### 배포 URL https://jennapark.netlify.app/ 📌현재 배포는 비용적 문제로 일시중지된 상태입니다.
> ### API Server : https://api.fafago.link/
<br>


## Result
[https://www.youtube.com/watch?v=4XruulOYhS0](https://youtu.be/4XruulOYhS0)
