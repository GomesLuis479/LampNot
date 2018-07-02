# LampNot - Notification Lamp 
#### ([Project Video](https://www.youtube.com/watch?v=V0Xg72j4OvM&feature=youtu.be))
A lamp connected over the internet flashes colors based on notifications on a user’s phone (android). Hardware used: Raspberry Pi 3 Model B. Cloud service used: Ubidots.
The root directory contains an android studio project `/android_LampNot` and a python script `/raspberry_pi/rasp_pi.py` for Raspberry Pi. APK file is also available in the root directory. 

The project was implemented using a Raspberry Pi 3 Model B. To flash colors, RGB LEDs were controlled using software PWM. [Ubidots](https://app.ubidots.com/accounts/signin/) cloud service is used for message communication. Since Ubidots supports the MQTT protocol, the Raspberry Pi script uses the same to subscribe to the Ubidots broker.

###### Working
The Ubidots cloud service allows for the creation of **Devices** and **Variables**. *Variables* are entities that hold data in a particular *Device*. To control colors being flashed, the android app updates values of these variables as and when notifications are received/cleared. Since the Raspberry Pi script subscribes to the Ubidots broker, changes in these variables are detected as and when they are published by the broker. Thus obviating the need to repeatedly use GET requests to detect changes in variable values.

## Raspberry Pi Script 
The following fields need to be modified in the Raspberry Pi python Script:

###### 1.) Physical Pin Connections
```
pinRed = 7
pinGreen = 11
pinBlue = 13
```
These fields indicate the physical pins of the Raspberry Pi to which the RGB LEB terminals are connected

###### 2.) Ubidots Authentication
```
DEVICE_NAME = 'lamp_not'
VARIABLE_NAME = 'lamp'
TOKEN = 'A1E-vL7sdanfs7NyRyNZ2COokUvxsWqIJkGE2Nb0wVqABJ1N'
```
To get the above three fields:
- Sign-in to [Ubidots](https://app.ubidots.com/accounts/signin/)
- Create a Device
- Create a Variable in the device you just created

For Example, consider a Device named **lamp_not** and a variable **lamp** in it as shown in the screenshots below:

![fig1](https://github.com/GomesLuis479/LampNot/blob/master/readMe_pics/device.JPG)

---

![fig2](https://github.com/GomesLuis479/LampNot/blob/master/readMe_pics/variable.JPG)

---

The Token can be found by clicking your username in the upper-right corner and selecting API Credentials option.

![fig3](https://github.com/GomesLuis479/LampNot/blob/master/readMe_pics/token.JPG)

## Android App
Similarly, authentication details need to be entered in the android app too.

<img src="https://github.com/GomesLuis479/LampNot/blob/master/readMe_pics/android.png" alt="drawing" width="250px"/>

Notification access needs to be enabled for proper functioning. Select the *ENABLE / DISABLE* option in the main screen to do this.




