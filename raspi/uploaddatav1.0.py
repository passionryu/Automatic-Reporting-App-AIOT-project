import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import RPi.GPIO as GPIO
import time
import Adafruit_ADXL345
accel = Adafruit_ADXL345.ADXL345()

cred = credentials.Certificate('iot-dbproject-firebase.json')
firebase_admin.initialize_app(cred,{
    'databaseURL' : 'https://iot-dbproject-default-rtdb.asia-southeast1.firebasedatabase.app/'
})

ref = db.reference() #db 위치 지정
sensor_ref = ref.child('raspberrypi/sensor')

# 바퀴의 지름 (mm)
wheel_diameter = 100
reed_count = 0
push_count = 1

def upload(speed1, speed2, tilt_z):
    global push_count
    sensor_ref.update({
        'data'+str(push_count):{
		'date' : time.strftime('%Y-%m-%d %H:%M:%S'),
		'speed1' : speed1,
        'speed2' : speed2,
		'tilt_z' : tilt_z,
	    }
    })
    push_count += 1

#함수 정의
def increment_count(channel):
    global reed_count
    reed_count += 1


def measure_speed():
    global reed_count
    distance = (reed_count * wheel_diameter * 3.141592/ 2) / 1000  # mm를 m로 변환
    # 속도 계산 (1초당 거리를 미터로 계산)
    speed = distance / 1 * 3.6 #m/s 를 km/h로 변환
    reed_count = 0  # 감지 횟수 초기화
    return round(speed, 1)

def measure_tilt():
    x,y,z = accel.read()
    return z

#GPIO setup
reed_switch_pin = 17

GPIO.setmode(GPIO.BCM)
GPIO.setup(reed_switch_pin, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.add_event_detect(reed_switch_pin, GPIO.FALLING, callback=increment_count, bouncetime=10)

try:
    time.sleep(1)
    speed1 = measure_speed()
    print ("속력: ", speed1)
    time.sleep(1)
    speed2 = measure_speed()
    print ("속력: ", speed2)
    z = measure_tilt()    
    print ("가속도 z: ", z)

    ai_result = False 
    if (ai_result == True):
        upload(speed1, speed2, z)
            

except KeyboardInterrupt:
    GPIO.cleanup()