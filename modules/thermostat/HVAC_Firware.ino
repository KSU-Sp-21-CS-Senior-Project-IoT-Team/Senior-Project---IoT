#include <DHT.h>
#define DHTTYPE DHT11 
#include <WiFi.h>
#include <HTTPClient.h>
#include <Arduino_JSON.h>





String httpGETRequest(const char* serverName) {
  HTTPClient http;
     
  // Your IP address with path or Domain name with URL path 
  http.begin(serverName);
 

  // Send HTTP POST request
  int httpResponseCode = http.GET();
  
  String payload = "{}"; 
  
  if (httpResponseCode>0) {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    payload = http.getString();
  }
  else {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
  // Free resources
  http.end();

  return payload;
}













//8888888888888888888888888888888888888888HTTP8888888888888888888888888888888888888888888888888888888888888888

const char* ssid = "12345";
const char* password = "ligma123";

//Your Domain name with URL path or IP address with path
const char* serverName = "http://sabernet.ddns.net:8080/api/devices/9be0a1cf-d7d9-48f4-9122-26a8e295cae3/schedules";

// the following variables are unsigned longs because the time, measured in
// milliseconds, will quickly become a bigger number than can be stored in an int.
unsigned long lastTime = 0;
unsigned long lastTime1 = 0;
// Timer set to 10 minutes (30000)
unsigned long timerDelay = 30000;


String APIread;



const int acRELAY_PIN = 4;
const int heatRELAY_PIN = 5;
const int  DHTPin = 21;

double sensorReadings1;

double Temperature;
int Humidity;

JSONVar keys;

const char* sensorReadings2 = "zed";

void setup() {
  Serial.begin(115200);






  //3volt
  DHT dht(DHTPin, DHTTYPE);


  pinMode(acRELAY_PIN, OUTPUT);
  pinMode(heatRELAY_PIN, OUTPUT);
  pinMode(DHTPin, INPUT);

  dht.begin();

   


   Temperature = dht.readTemperature(); 
   Temperature = (Temperature * 9/5) + 32;    // Gets the values of the temperature inf F
   Humidity = dht.readHumidity();
   Serial.println("temp  :" );
   Serial.println(Temperature);
   Serial.println("humidity");
   Serial.println(Humidity);















  WiFi.begin(ssid, password);
  Serial.println("Connecting");
  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to WiFi network with IP Address: ");
  Serial.println(WiFi.localIP());
 
  
}

void loop() {
   if(digitalRead(heatRELAY_PIN) == LOW && digitalRead(acRELAY_PIN) == LOW){
  digitalWrite(heatRELAY_PIN, HIGH);
  digitalWrite(acRELAY_PIN, HIGH);
   }

  



  if ((millis() - lastTime) > timerDelay) {
    //Check WiFi connection status
    if(WiFi.status()== WL_CONNECTED){
              
      APIread = httpGETRequest(serverName);
      //JSONVar myArray = JSON.parse(APIread);
      Serial.println(APIread);
      JSONVar myObject = JSON.parse(APIread);
     
      Serial.println("Object type:");
      Serial.println(JSON.typeof(myObject));
  
      // JSON.typeof(jsonVar) can be used to get the type of the var
      if (JSON.typeof(myObject) == "undefined") {
        Serial.println("Parsing input failed!");
        Serial.println(myObject);
        return;
      }
    
      Serial.print("JSON object = ");
      Serial.println(myObject);
    
       //myArray[0].keys() 
       //JSONVar myObject = myArray[0];
       //can be used to get an array of all the keys in the object
      keys = myObject.keys();
      Serial.println((const char*) myObject["mode"]);
     JSONVar value1 = (myObject[keys[0]]);
     JSONVar value2 = (myObject[keys[1]]);

     sensorReadings1 = (double)value1;
     sensorReadings2 = (const char*)value2;

     
      
      Serial.println(keys.length());
      Serial.print("Temperature = ");
      Serial.println(sensorReadings1);
      Serial.print("MODE = ");
      Serial.println(sensorReadings2);

    }
    else {
      Serial.println("WiFi Disconnected");
    }
    lastTime = millis();
  }


    
    // Serial.println("test function");
   //Serial.println(Temperature);
   // Serial.println(sensorReadings2[0]);
    const char* heatKey = "heat";
    const char* coolKey = "cool";


       if ((millis() - lastTime1) > timerDelay+10) {
    //check if we have data from the api to evaluate
    if(keys.length() == 2){
    if(sensorReadings2[0] == 'h' && sensorReadings1 > Temperature){
      digitalWrite(heatRELAY_PIN, LOW);
      Serial.print("heat is turned on: SET  = " );
      Serial.println( sensorReadings2);
      Serial.print(" Local Temperature = ");
      Serial.println(Temperature);
      Serial.print(" Set Temperature = ");
      Serial.println(sensorReadings1);
    } 
    else if(sensorReadings2[0] == 'h' && sensorReadings1 < Temperature) {
      digitalWrite(heatRELAY_PIN, HIGH);
      Serial.print("heat is turned off: SET = " );
      Serial.println( sensorReadings2);
      Serial.print(" Local Temperature = ");
      Serial.println(Temperature);
      Serial.print(" Set Temperature = ");
      Serial.println(sensorReadings1);
    }


    else if(sensorReadings2[0] == 'c' && sensorReadings1 < Temperature){
      digitalWrite(acRELAY_PIN, LOW);
      Serial.print("cool is turned on: SET = " );
      Serial.println( sensorReadings2);
      Serial.print(" Local Temperature = ");
      Serial.println(Temperature);
      Serial.print(" Set Temperature = ");
      Serial.println(sensorReadings1);
    }
    else if(sensorReadings2[0] == 'c' && sensorReadings1 > Temperature){
      digitalWrite(acRELAY_PIN, LOW);
      Serial.print("cool is turned off: SET = " );
      Serial.println( sensorReadings2);
      Serial.print(" Local Temperature = ");
      Serial.println(Temperature);
      Serial.print(" Set Temperature = ");
      Serial.println(sensorReadings1);
    }
    
    
    else{
      digitalWrite(acRELAY_PIN, HIGH);
      digitalWrite(heatRELAY_PIN, HIGH);
    }

    }
    lastTime1 = millis();
       }
  
}





