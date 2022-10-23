
# 기업연계형 파이널 프로젝트

### Description

```
 인공지능과 영상기술의 융합을 통해 영상 안에서 시간과 공간, 능력을 제공하기 위해 모였습니다.
```

<br>

> 프로젝트 명 <br>
**JenaPark**

> 프로젝트 기간 <br>
`2022.09.06 - 2022.10.14`

> 프로젝트 팀원 👪

- **UIUX**
  - 김유찬, 김동관, 이승현
- **FE**
  - 김민구, 김수현, 김채욱, 신재일
- **BE**
  - 김윤겸([@yoongyum](https://github.com/yoongyum)), 이창희([@WindowH22](https://github.com/WindowH22)), 강소영([@soyoungkangme](https://github.com/soyoungkangme))

> URL
- Client : https://jennapark.netlify.app/
- API Server : https://api.fafago.link/

## Env

> ### FE - [Repo](https://github.com/FinalProject-AIPARK/JenaPark-FE)
`React`, `Redux`, `Vite`, `TypeScript` <br><br>


> ### BE
`Java11`, `Spring Data JPA`, `Spring Security`, `JWT`, `Redis`, `MySQL`, `Swagger`, `OAuth2` <br>

`Python3`, `gTTS`, `boto3`, `moviepy`, `scipy`, `numpy`

#### OpenAPI
`Google Login`, `Kakao Login`

#### Server
`EC2`, `S3 Bucket`, `AWS Certification Manager`, `CloudFront`, `ELB(Application Load Balancer)`, `RDS`, `Route53`

#### CI/CD
`Jenkins`, `Docker`

> ### ETC
`git`, `Discord`, `Slack`, `Post Man`, `Docker DeskTop`

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

## DB Diagram
![image](https://user-images.githubusercontent.com/72365815/195251816-4f673977-1e3a-4ecc-b0b5-da4040172b80.png)

## Communication

Discord를 주로 사용하여 UIUX/FE/BE 팀원들과 실시간으로 소통을 원활하게 이루었습니다.
![image](https://user-images.githubusercontent.com/72365815/195583261-bafc3c4d-85d7-4882-bf23-409a89b17e3b.png)


## Role

이창희 (전체 팀장)

김윤겸 (BE 팀장)

강소영

<br>

## API 명세서

## Result
https://www.youtube.com/watch?v=4XruulOYhS0
