2023년도 P-project sw 개발 경연대회

총 28개 팀 中 최우수팀 

킥라니 구조대
![킬라니 구조대 로고](https://github.com/user-attachments/assets/641c6e51-8a62-48ee-8da3-ab898274c3ae)


#대회명: P-project sw개발 경연대회

#제작물: 교통사고 발생 시 자동/ 수동 신고 시스템

#팀원: 류성열(팀장), 장찬진, 손현철, 안지훈

팀 소개
![팀소개](https://github.com/user-attachments/assets/cb177f61-2b68-4017-8479-6160e2c1f322)

![프로젝트 설계도](https://github.com/user-attachments/assets/360091aa-cc9a-4956-a9d1-6380df1fe346)


서비스 내용 
![프로젝트 기획](https://github.com/user-attachments/assets/1acf4340-d26e-4264-9e90-c7c65b2e7142)


#킥보드에 라즈베리 파이를 중심으로 기울기 측정 센서,속도 측정 센서, 진동 센서 등을 부착하여 킥보드 주행 시 발생하는 속도, 기울기 , 진동 변화등을 감지

#감지한 센싱데이터를 여러가지 사고 상황을 가정한 파이썬 알고리즘에 대입한 후 결과 값을 사전 학습된 AI 모델에 주입 후 AI 모델이 사고인지 아닌지 판단

#AI 모델이 센싱데이터를 기반으로 사고라 판단하면 119와 사전 등록한 지인 휴대전화에 현재 사고지점 위치와 사고발생 사실에 대한 메시지를 자동 전송

#만약 사고가 나지 않았는데 AI 모델이 잘못 판단하여 사고발생 메시지를 보내려 할 시, 5초 이내에 전송취소 버튼을 누르면 전송이 취소 된다.

#만약 사고가 났는데, AI모델이 잘못 판단하여 메시지를 보내지 않을시 주변 사람이 직접 전화하여 현재위치를 말할 필요 없이, 수동신고 버튼을 누르면 자동으로 현재 위치가 구조대와 지인들에게 전송된다.
![디바이스 기능](https://github.com/user-attachments/assets/df27893a-e760-4ac0-9d8b-562b244ec4bd)
![사용자 기능](https://github.com/user-attachments/assets/96db5599-6804-4453-af5f-2d517a0d8811)
![관리자 기능](https://github.com/user-attachments/assets/0be04c9b-184b-47fa-b1fd-6f7dd4465270)





