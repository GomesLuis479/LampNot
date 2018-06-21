import threading
import time
import RPi.GPIO as GPIO
import paho.mqtt.client as mqtt

GPIO.setwarnings(False)

#ENTER THE FOLLOWING DETAILS******

#Pin Connection Details...

pinRed = 7
pinGreen = 11
pinBlue = 13

#UBIDOTS Details...

DEVICE_NAME = 'lamp_not'
VARIABLE_NAME = 'lamp'
TOKEN = 'A1E-vL7hus7NyRyNZ2COokUvxsWqIJkGE2Nb0wVqABJ1N'

#*********************************
try:
	class LedHub(threading.Thread):
	    def __init__(self, pinRed, pinGreen, pinBlue):
	        threading.Thread.__init__(self)
	        self.data = { 'Red' : 0 , 'Green' : 0 ,'Blue' : 0}
	        self.pwmRed = GPIO.PWM(pinRed, 50)
	        self.pwmGreen = GPIO.PWM(pinGreen, 50)
	        self.pwmBlue = GPIO.PWM(pinBlue, 50)
	        
	    def run(self):
	        print ("Starting Thread LedHub")
	        self.runLeds()
	        print ("Exiting Thread")

	    def sendData(self, num):
	        print('Data received : ' + str(num))
	        if(num == 11):
	            self.data['Red'] = 1

	        if(num == 10):
	            self.data['Red'] = 0

	        if(num == 21):
	            self.data['Green'] = 1

	        if(num == 20):
	            self.data['Green'] = 0

	        if(num == 31):
	            self.data['Blue'] = 1

	        if(num == 30):
	            self.data['Blue'] = 0
	        print(self.data)


	    def runLeds(self):

	        maxi = 100
	        delay = 0.01
	        
	        while True:
	            if(int(self.data['Red']) == 1):
	                
	                self.pwmRed.start(0)
	                
	                for i in range(maxi):
	                    self.pwmRed.ChangeDutyCycle(i)
	                    time.sleep(delay)
	    
	                for i in range(maxi):
	                    self.pwmRed.ChangeDutyCycle(maxi-i)
	                    time.sleep(delay)

	                self.pwmRed.stop()

	            
	            if(int(self.data['Green']) == 1):
	                
	                self.pwmGreen.start(0)
	                
	                for i in range(maxi):
	                    self.pwmGreen.ChangeDutyCycle(i)
	                    time.sleep(delay)
	    
	                for i in range(maxi):
	                    self.pwmGreen.ChangeDutyCycle(maxi-i)
	                    time.sleep(delay)

	                self.pwmGreen.stop()

	            if(int(self.data['Blue']) == 1):
	                
	                self.pwmBlue.start(0)
	                
	                for i in range(maxi):
	                    self.pwmBlue.ChangeDutyCycle(i)
	                    time.sleep(delay)
	    
	                for i in range(maxi):
	                    self.pwmBlue.ChangeDutyCycle(maxi-i)
	                    time.sleep(delay)

	                self.pwmBlue.stop()

	            time.sleep(0.5)


	GPIO.setmode(GPIO.BOARD)
	GPIO.setup(pinRed, GPIO.OUT)
	GPIO.setup(pinGreen, GPIO.OUT)
	GPIO.setup(pinBlue, GPIO.OUT)

	GPIO.output(pinRed, 0)
	GPIO.output(pinGreen, 0)
	GPIO.output(pinBlue, 0)

	Thread1 = LedHub(pinRed, pinGreen, pinBlue)
	Thread1.start()


	def on_connect(client, userdata, flags, rc):
	    print("Connected with result code "+str(rc))
	    print('flags : ', flags)
	    client.subscribe('/v1.6/devices/' + DEVICE_NAME + '/' + VARIABLE_NAME + '/lv', qos=1)
	    print('done')

	def on_message(client, userdata, msg):
		Thread1.sendData(int(msg.payload))
		
	client = mqtt.Client()
	client.on_connect = on_connect
	client.on_message = on_message


	client.username_pw_set(username = TOKEN, password="")
	client.connect("things.ubidots.com", 1883, 60)
	client.loop_forever()
                
            
except KeyboardInterrupt:
	pass


Thread1.pwmRed.stop()
Thread1.pwmGreen.stop()
Thread1.pwmBlue.stop()

GPIO.output(pinRed, 0)
GPIO.output(pinGreen, 0)
GPIO.output(pinBlue, 0)
GPIO.cleanup()
        
            
      
    
    
