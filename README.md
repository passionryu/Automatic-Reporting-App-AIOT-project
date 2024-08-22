<h1>2023년도 P-project sw 개발 경연대회</h1>

![킬라니 구조대 로고](https://github.com/user-attachments/assets/641c6e51-8a62-48ee-8da3-ab898274c3ae)


대회명: P-project sw개발 경연대회 -총 28개 팀 中 최우수팀 수상

제작물: 교통사고 발생 시 자동/ 수동 신고 시스템

팀원: 류성열(팀장), 장찬진, 손현철, 안지훈

<h3>팀 소개</h3>

![팀소개](https://github.com/user-attachments/assets/cb177f61-2b68-4017-8479-6160e2c1f322)


<h3>서비스 내용 </h3>

![프로젝트 기획](https://github.com/user-attachments/assets/1acf4340-d26e-4264-9e90-c7c65b2e7142)

<h5>로그인, 회원 가입, 사용자 화면, 전화번호 추가</h5>
<p align="center">
  <img src="https://github.com/user-attachments/assets/3eb56e68-24fa-4029-a520-5d69ee611fe5" width="22%">
  <img src="https://github.com/user-attachments/assets/bc7d2f0a-84a2-44f1-8262-25239c27fcf0" width="22%">
  <img src="https://github.com/user-attachments/assets/8ddb1efa-7e88-4f6e-a66c-4d9c6c7a0493" width="22%">
  <img src="https://github.com/user-attachments/assets/c33a3261-0d1f-4873-af78-e6794f9aa90e" width="22%">
</p>

<h5>회원 정보 수정, 회원 조회 , 기록 조회, 사고기록 조회</h5>
<p align="center">
  <img src="https://github.com/user-attachments/assets/29690403-bfcb-4ce0-866f-21b679b9d4b0" width="22%">
  <img src="https://github.com/user-attachments/assets/9d59cdfb-aed0-47a7-8f41-d99138fb5d37" width="22%">
  <img src="https://github.com/user-attachments/assets/58b9391a-d759-4638-990e-f45ec8f3ad6e" width="22%">
  <img src="https://github.com/user-attachments/assets/f8cb1434-cd3c-43f3-8f40-a566b2f00ec8" width="22%">
</p>



<h3>디바이스 기능</h3>

![디바이스 기능](https://github.com/user-attachments/assets/df27893a-e760-4ac0-9d8b-562b244ec4bd)
<h3>사용자 기능</h3>

![사용자 기능](https://github.com/user-attachments/assets/96db5599-6804-4453-af5f-2d517a0d8811)
<h3>관리자 기능</h3>

![관리자 기능](https://github.com/user-attachments/assets/0be04c9b-184b-47fa-b1fd-6f7dd4465270)


<h2>작동 원리 </h2>
킥보드에 라즈베리 파이를 중심으로 기울기 측정 센서,속도 측정 센서, 진동 센서 등을 부착하여 킥보드 주행 시 발생하는 속도, 기울기 , 진동 변화등을 감지

감지한 센싱데이터를 여러가지 사고 상황을 가정한 파이썬 알고리즘에 대입한 후 결과 값을 사전 학습된 AI 모델에 주입 후 AI 모델이 사고인지 아닌지 판단

AI 모델이 센싱데이터를 기반으로 사고라 판단하면 119와 사전 등록한 지인 휴대전화에 현재 사고지점 위치와 사고발생 사실에 대한 메시지를 자동 전송

만약 사고가 나지 않았는데 AI 모델이 잘못 판단하여 사고발생 메시지를 보내려 할 시, 5초 이내에 전송취소 버튼을 누르면 전송이 취소 된다.

만약 사고가 났는데, AI모델이 잘못 판단하여 메시지를 보내지 않을시 주변 사람이 직접 전화하여 현재위치를 말할 필요 없이, 수동신고 버튼을 누르면 자동으로 현재 위치가 구조대와 지인들에게 전송된다.




