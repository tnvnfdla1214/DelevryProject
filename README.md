# 배달의 민족 프로젝트

본 프로젝트는 배달의 민족을 모티브로 제작된 위치기반 음식 주문 어플리케이션입니다. 제작의도로는 아키텍처와 디자인패턴, Jetpack를 경험하고 학습하기 위해 진행하였습니다.
프로젝트를 들어가기 전 다양한 프로젝트를 해보면서 아키텍처와 디자인 패턴에 대한 부재로 인한 문제점을 이해하게 되었고 아키텍처와 디자인 패턴을 설계 및 학습하기 위해 작은 프로젝트를 진행했습니다. 배달의 민족 어플리케이션의을 참고하여 UI에 신경을 썼으며 이전에 SK-API의 정보를 사용하여 인근 가게의 정보를 받아옵니다. 또한 이전 사용해보았던 Koin대신 Google에서 권장하는 Hilt를 적용하여 개발했습니다


### :wrench: 기능설명

+ Firebase를 활용한 소셜 로그인 인증 코튼 관리 및 내 정보 화면 구현
+ 커스텀 뷰와 ViewPager2를 사용하여 움직이는 메인 화면 구현
+ SK-API를 연동하여 음식점 목록화면, 상세화면 구현
+ 찜 가게, 장바구니에 담은 정보를 Firestore에 저장 및 관리
+ 찜 한 가게 화면, 주문내역 화면 구현
+ 공유하기 기능
+ Android Architecture Guide 설계
+ Hilt 사용
+ Coroutine 사용
+ MVVM Patten, State Patten 설계

### 📜 깨달은 점
+ Google Android Architecture Guide에 맞춰 개발하면서 아키텍처에 대해 이해하게 되었습니다.
+ 커스텀 뷰와 ViewPager2를 사용하여 움직이는 UI를 만들어보면서 이전 사용해봤던 커스텀 뷰에서 보다 자유로운 안드로이드 UI를 학습할 수 있었습니다.
+ 디자인 패턴에 대한 이해도가 높아졌습니다.
+ 이전 사용하였던 Koin대신 Hilt를 적용하여 개발하여 DI에 대한 이해도가 더욱 높아졌습니다.


***
### :lollipop: 완성 화면
#### 메인 화면
![움직이는 짤2](https://user-images.githubusercontent.com/48902047/169704816-233e1813-0c83-47b6-bf54-8c15af5a35c8.gif)
#### 네비게이션 화면
<img src = "https://user-images.githubusercontent.com/48902047/156923668-6b20d53f-8b14-4b82-9c28-7af08a2b811e.jpg" width="20%" height="20%">  <img src = "https://user-images.githubusercontent.com/48902047/156923726-321e570c-1d10-487e-9ad9-f54b9a214602.jpg" width="20%" height="20%">  <img src = "https://user-images.githubusercontent.com/48902047/156923751-2106c77e-524d-4e1d-b60d-5fbc21f59393.jpg" width="20%" height="20%">  <img src = "https://user-images.githubusercontent.com/48902047/156923754-5eb898d3-c3c1-426d-9023-9e7df9a72663.jpg" width="20%" height="20%">

#### 내 위치 화면
<img src = "https://user-images.githubusercontent.com/48902047/169704982-88adce0a-353e-4b0d-a440-759e65ec5624.jpg" width="20%" height="20%">  <img src = "https://user-images.githubusercontent.com/48902047/169705011-81318b9f-0736-47d6-9e6d-471758b24cc2.jpg" width="20%" height="20%">  <img src = "https://user-images.githubusercontent.com/48902047/169705018-95ab6e13-7bc2-47a0-b2d9-03ba2904dd0e.jpg" width="20%" height="20%">

#### 음식점 화면
<img src = "https://user-images.githubusercontent.com/48902047/169705050-3491e5ea-ad58-402e-b3d0-b475f31e0c34.jpg" width="20%" height="20%">  <img src = "https://user-images.githubusercontent.com/48902047/169705076-204111b0-ed70-4fe1-b3c8-2bf9b7377569.jpg" width="20%" height="20%">  <img src = "https://user-images.githubusercontent.com/48902047/169705124-49d657e3-abbc-4efb-ab4c-f36583e89e27.jpg" width="20%" height="20%">

## 기술 Stack
### Android Architecture Guide와 MVVM, Jetpack
저는 아키텍처와 디자인패턴을 위해 많은 검색과 자료를 찾아보았습니다. 그 중에서. '드로이드 나이츠'에서 MVVM 강의를 하였던 "Grap"의 정승욱 개발자님의 강의를 보고 의문점을 자문을 이메일로 보냈고 회신의 내용의 가장 중점적인 내용은 "구글 아키텍처와 MVVM은 다르다"와 "현재로서는 MVVM를 토대로 개발한 프로젝트 공유는 할 수 없다"였습니다. 결국 예시 없이는 확실한 MVVM 체계를 개발할 수 없었고 MVVM을 고집할수록 구글 아키텍처와 멀어진다고 판단하였으므로 확실하게 나와있는 Google Android Architecture Guide기본으로 잡고 MVVM이 추구하는 방향성을 최대한 맞추려고 노력하였습니다. 아래는 제가 설계한 프로젝트 도면입니다.

<p align = center>
   <img src = "https://user-images.githubusercontent.com/48902047/169705317-d09fe270-a58a-42a6-a26a-036b9f7511f9.png" width="80%" height="80%">
</p>

그 중 UI 레이어에 State Patten을 적용시켜 비즈니스 로직을 용이하게 하려 노력하였습니다.

<p align = center>
   <img src = "https://user-images.githubusercontent.com/48902047/169705405-57649a02-0756-4500-af64-6d9399100be6.png" width="80%" height="80%">
</p>

### DI
<p align = center>
   <img src = "https://user-images.githubusercontent.com/48902047/169705427-4c50b13c-4d16-47a8-b4f4-983a3efde8f2.png" width="50%" height="50%"> <img src = "https://user-images.githubusercontent.com/48902047/169705445-73bdaf3c-abf1-48cb-ba21-950916979eb7.png" width="40%" height="40%">
</p>

왼쪽은 Koin을 사용했던 appModule입니다. compile 단계에서 확인해 볼 수 있고 구글에서 추천하는 Hilt로 변경하였습니다. 오른쪽은 Hilt로 변경한 이번 프로젝트입니다.

### Coroutine을 사용한 비동기화
본 프로젝트에서는 네트워크 작업에 관련된 작업(Firebase 데이터 작업, API 자료 불러오기)등에서 사용되었습니다. 대표적인 Rxjava와 Coroutine중 Coroutine을 결정하게 된 이유는 학습 곡선이 완만하기에 선택하였습니다.

## 추후 리펙토리 할 것
1. LiveData -> Livestate 로 변경
- LiveData가 여러개로 나뉘어 있는데 좀더 합치기
2. 리뷰 액티비티 만들기
3. 디자인 좀더 신경 쓰기 
