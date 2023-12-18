import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import time
import Adafruit_ADXL345
import numpy as np
import joblib
from sklearn.model_selection import GridSearchCV
from sklearn.tree import DecisionTreeClassifier


accel = Adafruit_ADXL345.ADXL345()

cred = credentials.Certificate('/home/pi123/PythonHome/pproject/iot-dbproject-chan.json')
firebase_admin.initialize_app(cred,{
    'databaseURL' : 'https://fir-test-bc570-default-rtdb.firebaseio.com/'
})

ref = db.reference() #db 위치 지정
sensor_ref = ref.child('firebasetest/RawLogs')
notify_ref = ref.child('notify')

# 바퀴의 지름 (mm)
wheel_diameter = 200
reed_count = 0
push_count = 1
flag = False
button01 = False
speed_temp = 0
isFirst = 1

def upload(speed1, speed2, tilt_z):
    global push_count
    print("uploaded")
    notify_ref.update({'notify' : 'data'+str(push_count)})
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
    distance = (reed_count * wheel_diameter * 3.141592) / 1000  # mm를 m로 변환
    # 속도 계산 (1초당 거리를 미터로 계산)
    speed = distance / 1 * 3.6 #m/s 를 km/h로 변환
    reed_count = 0  # 감지 횟수 초기화
    return round(speed, 1)

def measure_tilt():
    x,y,z = accel.read()
    return z

def button1_callback(channel):
    global button01
    print("Button 1 pressed")
    button01 = True

def button2_callback(channel):
    global flag
    print("Button 2 pressed")
    GPIO.output(led_pin, GPIO.LOW)  # Turn on the LED
    flag = True


#GPIO setup
#reed_switch_pin = 17
reed_switch_pin2 = 23
button1 = 15
button2 = 25
led_pin = 18  # GPIO pin for the LED

GPIO.setmode(GPIO.BCM)
#GPIO.setup(reed_switch_pin, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(reed_switch_pin2, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(button1, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(button2, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(led_pin, GPIO.OUT)

#GPIO.add_event_detect(reed_switch_pin, GPIO.FALLING, callback=increment_count, bouncetime=10)
GPIO.add_event_detect(reed_switch_pin2, GPIO.FALLING, callback=increment_count, bouncetime=10)
GPIO.add_event_detect(button1, GPIO.FALLING, callback=button1_callback, bouncetime=10)
GPIO.add_event_detect(button2, GPIO.FALLING, callback=button2_callback, bouncetime=10)

ss = joblib.load('/home/pi123/PythonHome/pproject/ss.pkl')
dt_clf = joblib.load('/home/pi123/PythonHome/pproject/dt_clf.pkl')

try:
    while True:
        GPIO.output(led_pin, GPIO.LOW)
        sensing =[[]]
        if (isFirst == True):
            time.sleep(1)
            speed1 = measure_speed()
            isFirst = False
        else:
            speed1 = speed_temp
        print ("속력1: ", speed1)
        sensing[0].append(speed1)
        time.sleep(1)
        speed2 = measure_speed()
        speed_temp = speed2
        print ("속력2: ", speed2)
        sensing[0].append(speed2)
        z = measure_tilt()    
        print ("가속도 z: ", z)
        sensing[0].append(z)
    

        input_scaled = ss.transform(sensing)
        dt_clf_pred = dt_clf.predict(input_scaled)

        if (dt_clf_pred[0] == 1) or (button01 == True):
            print("교통사고 발생, led on")
            GPIO.output(led_pin, GPIO.HIGH)  # Turn on the LED

            time.sleep(5)
            if (flag == False):
                upload(speed1, speed2, z)

            flag = False
            button01 = False
            reed_count = 0
            GPIO.output(led_pin, GPIO.LOW)

            # Upload data to Firebase (this part can be adjusted based on your actual upload logic)
        


except KeyboardInterrupt:
    GPIO.cleanup()