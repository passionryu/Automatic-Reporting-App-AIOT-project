import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import RPi.GPIO as GPIO
import time
import Adafruit_ADXL345
import numpy as np
import joblib
from sklearn.model_selection import GridSearchCV
from sklearn.tree import DecisionTreeClassifier


accel = Adafruit_ADXL345.ADXL345()

cred = credentials.Certificate('/home/pi123/PythonHome/pproject/iot-dbproject-firebase.json')
firebase_admin.initialize_app(cred,{
    'databaseURL' : 'https://iot-dbproject-default-rtdb.asia-southeast1.firebasedatabase.app/'
})

ref = db.reference() #db 위치 지정
sensor_ref = ref.child('raspberrypi/sensor')

# 바퀴의 지름 (mm)
wheel_diameter = 200
reed_count = 0
push_count = 1
cancel_report = False
manual_report = False

def upload(speed1, speed2, tilt_z):
    global push_count
    print("uploaded")
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

def manual_report_callback(channel):
    global manual_report
    print("수동신고 button pressed")
    manual_report = True

def cancel_report_callback(channel):
    global cancel_report
    print("취소 button pressed")
    GPIO.output(led_pin, GPIO.LOW)  # Turn off the LED
    cancel_report = True


#GPIO setup
reed_switch_pin = 17
reed_switch_pin2 = 27
button1 = 15 # 수동 신고 버튼, 파란색
button2 = 23 # 신고 취소 버튼, 빨간색
led_pin = 18  # GPIO pin for the LED

GPIO.setmode(GPIO.BCM)
GPIO.setup(reed_switch_pin, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(reed_switch_pin2, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(button1, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(button2, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(led_pin, GPIO.OUT)

GPIO.add_event_detect(reed_switch_pin, GPIO.FALLING, callback=increment_count, bouncetime=10)
GPIO.add_event_detect(reed_switch_pin2, GPIO.FALLING, callback=increment_count, bouncetime=10)
GPIO.add_event_detect(button1, GPIO.FALLING, callback=manual_report_callback, bouncetime=10)
GPIO.add_event_detect(button2, GPIO.FALLING, callback=cancel_report_callback, bouncetime=10)

ss = joblib.load('/home/pi123/PythonHome/pproject/ss.pkl')
dt_clf = joblib.load('/home/pi123/PythonHome/pproject/dt_clf.pkl')

try:
    while True:
        GPIO.output(led_pin, GPIO.LOW)
        sensing =[[]]
        time.sleep(1)
        speed1 = measure_speed()
        print ("속력: ", speed1)
        sensing[0].append(speed1)
        time.sleep(1)
        speed2 = measure_speed()
        print ("속력: ", speed2)
        sensing[0].append(speed2)
        z = measure_tilt()    
        print ("가속도 z: ", z)
        sensing[0].append(z)
    

        input_scaled = ss.transform(sensing)
        dt_clf_pred = dt_clf.predict(input_scaled)

        if (dt_clf_pred[0] == 1) or (manual_report == True):
            print("교통사고 발생, led on")
            GPIO.output(led_pin, GPIO.HIGH)  # Turn on the LED

            time.sleep(5)
            if (cancel_report == False):
                upload(speed1, speed2, z)

            cancel_report = False
            manual_report = False
            GPIO.output(led_pin, GPIO.LOW)

            # Upload data to Firebase (this part can be adjusted based on your actual upload logic)
        


except KeyboardInterrupt:
    GPIO.cleanup()