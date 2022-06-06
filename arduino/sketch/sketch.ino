#include <SoftwareSerial.h>

// Bluetooth Serial
SoftwareSerial MyBlue(2,3);

// Chars to wait for in the bluetooth communication
char ADELANTE = 'u';
char ATRAS = 'd';
char PARAR = 's';
char IZQUIERDA = 'l';
char DERECHA = 'r';

// Pins for the two groups of motors (PWM pins required)
// Pin Group 1 forward
int pinG1Ad = 5;
// Pin Group 1 backward
int pinG1At = 6;
// Pin Group 2 forward
int pinG2Ad = 10;
// Pin Group 1 backward
int pinG2At = 11;

void setup() {
  // Initializa arduino serial communication
  Serial.begin(9600);
  // Initialize software serial for bluetooth
  MyBlue.begin(9600);

  Serial.println("Ready to connect\nDefault is 000 or 1234");

  // Initialize pins as output
  pinMode(pinG1Ad, OUTPUT);
  pinMode(pinG1At, OUTPUT);
  pinMode(pinG2Ad, OUTPUT);
  pinMode(pinG2At, OUTPUT);
}

void adelante(){
  Serial.println("Adelante");
  analogWrite(pinG1Ad, 0);
  analogWrite(pinG1At, 0);
  analogWrite(pinG2Ad, 0);
  analogWrite(pinG2At, 0);
  delay(500);
  analogWrite(pinG1Ad, 200);
  analogWrite(pinG2Ad, 200);
}

void parar(){
  Serial.println("Parar");
  analogWrite(pinG1Ad, 0);
  analogWrite(pinG1At, 0);
  analogWrite(pinG2Ad, 0);
  analogWrite(pinG2At, 0);
  delay(500);
}

void atras() {
  Serial.println("Atras");
  analogWrite(pinG1Ad, 0);
  analogWrite(pinG1At, 0);
  analogWrite(pinG2Ad, 0);
  analogWrite(pinG2At, 0);
  delay(500);
  analogWrite(pinG1At, 200);
  analogWrite(pinG2At, 200);
}

void izquierda(){
  Serial.println("Izquierda");
  analogWrite(pinG1Ad, 0);
  analogWrite(pinG1At, 0);
  analogWrite(pinG2Ad, 0);
  analogWrite(pinG2At, 0);
  delay(500);
  analogWrite(pinG1At, 100);
  analogWrite(pinG2Ad, 100);
}

void derecha() {
  Serial.println("Derecha");
  analogWrite(pinG1Ad, 0);
  analogWrite(pinG1At, 0);
  analogWrite(pinG2Ad, 0);
  analogWrite(pinG2At, 0);
  delay(500);
  analogWrite(pinG2At, 100);
  analogWrite(pinG1Ad, 100);
}

void loop() {
  if (MyBlue.available()){
    char data = MyBlue.read();
    if(data == ADELANTE){
      adelante();
    }else if(data == ATRAS) {
      atras();
    }else if(data == PARAR) {
      parar();
    }else if(data == IZQUIERDA) {
      izquierda();
    }else if(data == DERECHA) {
      derecha();
    }
  }
  delay(1000);
}
