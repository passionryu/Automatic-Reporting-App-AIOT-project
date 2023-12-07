import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import RPi.GPIO as GPIO
import time
import Adafruit_ADXL345
accel = Adafruit_ADXL345.ADXL345()

cred = credentials.Certificate('firebase_key') #need key.json 
firebase_admin.initialize_app(cred,{
    'databaseURL' : 'realtime_db_URL' #need URL 
})

ref = db.reference() #db 위치 지정
sensor_ref = ref.child('raspberrypi/sensor')

# 바퀴의 지름 (mm)
wheel_diameter = 200
count = 0
push_count = 1

def upload(speed, tilt_z):
    global push_count
    sensor_ref.update({
        'data'+str(push_count):{
		'date' : '23-12-04',
		'speed' : speed,
		'tilt_z' : tilt_z,
	    }
    })
    push_count += 1

#함수 정의
def increment_count(channel):
    global count
    count += 1

#초음파 센서(거리) 미사용
# def measure_distance(trigger, echo): 
#     GPIO.output(trigger, False)
#     time.sleep(0.5) 

#     GPIO.output(trigger, True)
#     time.sleep(0.00001)
#     GPIO.output(trigger, False)
#     while GPIO.input(echo) == 0 :
#         pulse_start = time.time() 

#     while GPIO.input(echo) == 1 :
#         pulse_end = time.time() 
#         pulse_duration = pulse_end - pulse_start
#         distance = pulse_duration * 17000
#         distance = round(distance, 2) 
        
#     return distance

def measure_speed():
    global count
    # 지름*파이 = 둘레, 둘레의 반마다 리드스위치 설치하여 카운트를 2로 나눔
    distance = (count * wheel_diameter * 3.141592 / 2) / 1000  # mm를 m로 변환
    # 속도 계산 (0.5초당 거리를 미터로 계산), m/s 이므로 km/h 로 환산하려면 3.6을 곱함
    speed = distance / 0.5 * 3.6 # 
    count = 0  # 감지 횟수 초기화
    return round(speed, 1)

def measure_tilt():
    z = accel.read()
    return z

#GPIO setup
reed_switch_pin = 17
reed_switch_pin = 27
# trig = [13, 14, 15, 16]
# echo = [19, 20, 21, 22]

GPIO.setmode(GPIO.BCM)
GPIO.setup(reed_switch_pin, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.add_event_detect(reed_switch_pin, GPIO.FALLING, callback=increment_count, bouncetime=10)
GPIO.add_event_detect(reed_switch_pin2, GPIO.FALLING, callback=increment_count, bouncetime=10)
# GPIO.setup(trig, GPIO.OUT)
# GPIO.setup(echo, GPIO.IN)

try:
    while True:
        speed = measure_speed()
        z = measure_tilt()
        print ("속력: ", speed)
        print ("가속도 z: ", z)
        time.sleep(0.5)
        ai_result = False
        if (ai_result == True):
            upload(speed, z)
            

except KeyboardInterrupt:
    GPIO.cleanup()
