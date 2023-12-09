라즈베리파이 블루투스 활성화

sudo apt-get update
sudo apt-get install bluetooth
sudo apt-get install bluez
sudo service bluetooth start

sudo apt-get install python3-dev
sudo apt-get install bluetooth libbluetooth-dev
sudo pip install pybluez

Bluetooth 통신 코드 작성
--------------  가능성1:안드로이드 디바이스의 bluetooth 주소를 알때
import bluetooth

server_address = 'F0:F5:64:34:D8:23'  # 안드로이드 디바이스의 Bluetooth 주소로 변경
port = 1

sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
sock.connect((server_address, port))

message = "Hello"
sock.send(message)

sock.close()
------------ 가능성2:이 코드로 페어링이 가능할 때
import time
import bluetooth

def start_bluetooth_server():
    server_socket = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
    port = 1  # RFCOMM 포트 번호

    server_socket.bind(("", port))
    server_socket.listen(1)

    print("Bluetooth server is waiting for connection...")

    client_socket, client_address = server_socket.accept()
    print(f"Accepted connection from {client_address}")

    try:
        while True:
            #사고감지
            if accident = True:
                time.sleep(10) #여기서 버튼누르면 취소 판단
                server_socket.send("True")

    except Exception as e:
        print(f"Exception: {e}")

    finally:
        print("Closing connection.")
        client_socket.close()
        server_socket.close()

if __name__ == "__main__":
    start_bluetooth_server()
-----------------------------