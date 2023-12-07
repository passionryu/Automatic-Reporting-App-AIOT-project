import RPi.GPIO as GPIO
import time

# GPIO 핀 번호 설정
reed_switch_pin = 17
reed_switch_pin2 = 27

# 바퀴의 지름 (mm)
wheel_diameter = 200

# 리드스위치 감지 횟수 초기화
count = 0

def setup():
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(reed_switch_pin, GPIO.IN, pull_up_down=GPIO.PUD_UP)
    GPIO.add_event_detect(reed_switch_pin, GPIO.FALLING, callback=increment_count, bouncetime=10)
    GPIO.add_event_detect(reed_switch_pin2, GPIO.FALLING, callback=increment_count, bouncetime=10)
    

def increment_count(channel):
    global count
    count += 1

# def measure_speed():
#     try:
#         while True:
#             time.sleep(1)
#             # 주행 거리 계산
#             distance = (count * wheel_diameter * 3.141592) / 1000  # mm를 m로 변환
#             # 속도 계산 (1초당 거리를 미터로 계산)
#             speed = distance
#             print(f"주행 속도: {speed:.2f} m/s")
#             count = 0  # 감지 횟수 초기화
            
def measure_speed():
    global count
    # 반바퀴 마다 카운트 따라서 지름*파이/2 , /1000 = mm를 m로 변환
    distance = (count * wheel_diameter * 3.141592 / 2) / 1000  
    # 속도 계산 (0.5초당 거리를 미터로 계산)
    speed = distance / 0.5
    count = 0  # 감지 횟수 초기화
    print(f"주행 속도: {speed:.2f} m/s")
    return round(speed, 1)
    
    
try:
    while True:
        measure_speed()
        time.sleep(0.5)
except KeyboardInterrupt:
    pass
finally:
    GPIO.cleanup()
