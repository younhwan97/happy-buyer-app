# **코코마트 - HappyBuyer**

![앱 그래픽 이미지](https://github.com/younhwan97/happy-buyer-app/blob/develop/happy_buyer_app_graphic_img.png?raw=true)

- [🔗 Google Play 스토어](https://play.google.com/store/apps/details?id=kr.co.younhwan.happybuyer)

- [🔗 코코마트 웹 사이트](http://happybuyer.co.kr)

<br>

## 📌 HappyBuyer란?

> 동네마트 배달 앱

**코코마트 - HappyBuyer**는 부모님께서 운영하시는 마트에서 모바일을 통한 배달 서비스를 제공하기 위해 만들어진 앱 서비스입니다. 

실제 마트를 이용하는 주 연령층을 고려하여 **직관적 사용자 인터페이스**와 **간단한 주문 과정**을 통해 **누구나 이용하기 쉬운 앱**을 만들려고 노력했습니다.

🗓 개발 기간: 2021.12 ~ 2022.05

<br>

## 📷 핵심 기능 사진

<table>
  <tr>
    <td><img width="226px" height="452px" src="https://github.com/younhwan97/happy-buyer-app/blob/develop/happy_buyer_app_screen_shot.gif?raw=true"/></td>
    <td><img width="226px" height="452px" src="https://github.com/younhwan97/happy-buyer-app/blob/develop/happy_buyer_app_screen_shot_1.png?raw=true"/></td>
    <td><img width="226px" height="452px" src="https://github.com/younhwan97/happy-buyer-app/blob/develop/happy_buyer_app_screen_shot_2.png?raw=true"/></td>
  </tr>
  <tr>
     <td><img width="226px" height="452px" src="https://github.com/younhwan97/happy-buyer-app/blob/develop/happy_buyer_app_screen_shot_3.png?raw=true"/></td>
    <td><img width="226px" height="452px" src="https://github.com/younhwan97/happy-buyer-app/blob/develop/happy_buyer_app_screen_shot_4.png?raw=true"/></td>
    <td><img width="226px" height="452px" src="https://github.com/younhwan97/happy-buyer-app/blob/develop/happy_buyer_app_screen_shot_5.png?raw=true"/></td>
  </tr>    
 </table>

<br>

## 🚀 기술

- **Android:** `Kotlin` / `MVP` / `DataBinding`, `Coroutine`, `OkHttp3`, `Glide`, `ViewPager2`, `RecylerView`, `OAuth`
- **Front-end:** `HTML/CSS/JS`, `Bootstrap`
- **Back-end:** `Node.js`
- **AWS:** `EC2`, `RDS`, `S3`, `IAM`, `Route53`

<br>

## 🔥 성과

- 혼자서 6개월 동안 앱, 웹, 서버, DB를 기획/디자인/개발
- AWS EC2 환경에 배포 및 [플레이스토어](https://play.google.com/store/apps/details?id=kr.co.younhwan.happybuyer) 등록
- 사용자 **요구사항**을 적극 분석
  - 주요 타겟: 주부(연령대가 높음) → 직관성, 간단한 사용
  - 주문 품목: 반복됨 → 반복되는 상품 위주의 화면 구성 및 검색 기능 제공
  - 주문 시간: 점심, 저녁 식사 1~2시간 전에 집중 → 클라우드를 이용한 트래픽 관리
- 유지보수와 **서비스 확장성**을 고려하여 프레젠테이션 계층과 데이터 계층을 분리
- Coroutine과 OkHttp를 활용한 비동기 API 호출
- 상품 데이터를 가져올 때, **페이징 처리**를 이용하여 DB에 데이터가 아무리 많아지더라도 **일관된 앱 성능을 보장**
- 연령대가 높은 사용자도 쉽게 사용할 수 있도록 직관된(Material Design에 따른) UI 설계 및 최소 3개의 엑티비티만을 이용한 간단한 주문 과정 구현
- 소셜로그인과 사용자 인증 토큰 관리

<br>

## 🤔 배우고 느낀 점

- `MVP` 패턴을 처음 도입해보며, 패턴에 대한 이해도를 높잎 수 있었음.
- 개인 프로젝트로 진행한 만큼 모든 개발 과정을 직접 경험할 수 있었음.
- 연령대가 높은 사용자도 쉽게 사용할 수 있는 UI/UX 설계에 대하여 고민해볼 수 있었음.
- 사용자 요구분석 단계에서 명확한 요구사항을 발견하지 못해 앱 제작 과정에 어려움을 겪은 적이 있음. 따라서 이 프로젝트를 계기로 사용자 요구분석 단계의 중요성을 몸소 체험할 수 있었음.
- 프로젝트가 길어지고 웹, 앱, 서버 등에 걸쳐 여러 프로그래밍을 혼자서 하다 보니 코드 작성 방식이 달라지는 등의 문제가 발생하였는데, 해당 문제로 이후 리팩토링 등의 단계에서 어려움을 경험함. 따라서 이 프로젝트를 계기로 일관된 프로그래밍의 중요성을 알게 되었음.
