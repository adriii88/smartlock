// This #include statement was automatically added by the Particle IDE.
#include <HttpClient.h>

// This #include statement was automatically added by the Particle IDE.
#include "HC-SR04_Lib.h"



int lock = D2;
int alarm = D7;
bool locked = false;

int LOCK = 0;
int UNLOCK = 1;

//Sensor
int sensorvalue_cm;
int threshold = 10;
int trigPin = D0;
int echoPin = D3;

bool alarmFlag = false;

HC_SR04 rangeFinder = HC_SR04(trigPin, echoPin);

unsigned int nextTime = 0;
HttpClient* httpClient;
String jsonWeather = "";


void setup() {
    Particle.function("switch", lockToggle);
    Particle.function("alarmoff", turnOffAlarm);
    Particle.variable("alarmflag", alarmFlag);
    Particle.variable("weather", jsonWeather);  
    pinMode(alarm, OUTPUT);
    pinMode(lock, OUTPUT);
    digitalWrite(lock, UNLOCK);
    
    httpClient = new HttpClient();
}


void loop() {
    if (locked){
        if (!checkSensor()) {
            turnOnAlarm("");
        }
        delay(5000);
    }
    if (nextTime > millis()) {
        return;
    }
    
    make_request("Aarhus", "ae5f58cec27e70fe1ed601f1d026d576", "metric");
    //The location will be always the same for every smart Lock
    
    nextTime = millis() + 10000;
}


int lockToggle(String command) {
    
    if (command == "lock") {
        if (checkSensor()) {
            digitalWrite(lock, LOCK);
            locked = true;
            Particle.publish("debug", "Pointer was here");
            return 1;
        }
    }
    
    else if (command == "unlock") {
        digitalWrite(lock, UNLOCK);
        locked = false;
        return 0;
    }
    
    return -1;
}


bool checkSensor() {
    if ((sensorvalue_cm = rangeFinder.getDistanceCM()) > threshold) {
        Particle.publish("debug", "checkSensor FALSE");
        return false;
    } else {
        Particle.publish("debug", "checkSensor TRUE");
        return true;
    }
}

int turnOnAlarm(String x) {
    alarmFlag = true;
    digitalWrite(alarm, HIGH);
    return 1;
}

int turnOffAlarm(String x) {
    alarmFlag = false;
    digitalWrite(alarm, LOW);
    return 1;
}


void make_request(String location, String api_key, String units){
    http_request_t request;
    request.hostname = "api.openweathermap.org";
    request.port = 80;
    request.path = "/data/2.5/weather?q="
    + location
            + "&units=" + units // metric or imperial
      + "&cnt=1" // number of days
      + "&mode=json" // xml or json
      + "&APPID=" + api_key; // see http://openweathermap.org/appid
  request.body = "";

    http_response_t http_response;
    
    httpClient->get(request, http_response);
    
    jsonWeather = http_response.body;
    
    //Serial.print("Response body: ");
    //Serial.println(http_response.body);
}