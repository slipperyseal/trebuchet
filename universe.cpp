
#include "universe.h"


/*** trebuchet.equipment.Scanner ***/
void Scanner::scan() {
}

/*** trebuchet.equipment.Device ***/
Device::Device() {
}

void Device::explode() {
}

/*** trebuchet.equipment.LongRangeScanner$MyInnerScanner ***/
LongRangeScanner::MyInnerScanner::MyInnerScanner() {
    this->anotherNumber = 20;
}

void LongRangeScanner::MyInnerScanner::scanAhoy() {
    this->anotherNumber = this->anotherNumber + 4;
}

/*** trebuchet.equipment.LongRangeScanner$YourInnerScanner ***/
LongRangeScanner::YourInnerScanner::YourInnerScanner() {
    this->someNumber = 10;
}

void LongRangeScanner::YourInnerScanner::scanAhoy() {
    this->someNumber = this->someNumber + 2;
}

/*** trebuchet.equipment.LongRangeScanner ***/
LongRangeScanner::LongRangeScanner() {
}

int LongRangeScanner::getRange() {
    return this->range;
}

void LongRangeScanner::setRange(int range) {
    this->range = range;
}

void LongRangeScanner::scan() {
    this->range = this->range + 234;
    this->range = this->range - 123;
}

int LongRangeScanner::getDeviceId() {
    return 1337;
}

/*** trebuchet.craft.Spaceship ***/
int Spaceship::FIRSTSHIP = 100;

Spaceship::Spaceship(LongRangeScanner * longRangeScanner) {
    this->name = "Nostromo";
    this->serialNumber = 120;
    
    this->longRangeScanner = longRangeScanner;
}

LongRangeScanner * Spaceship::getLongRangeScanner() {
    return this->longRangeScanner;
}

void Spaceship::reset(LongRangeScanner * longRangeScanner, int serialNumber) {
    this->longRangeScanner = longRangeScanner;
    this->serialNumber = serialNumber;
}

/*** trebuchet.Universe ***/
Universe::Universe() {
}

void Universe::main(char * args) {
    ;
}

int main(int argc, char* argv[]) {
    Universe::main(0);
    return 0;
}

/*** java.lang.Object ***/
Object::Object() {
}

Object * Object::getClass() {
    return 0;
}

int Object::hashCode() {
    return 0;
}

bool Object::equals(Object * obj) {
    return false;
}

Object * Object::clone() {
    return 0;
}

const char * Object::toString() {
    return 0;
}

void Object::notify() {
}

void Object::notifyAll() {
}

void Object::wait(long long timeout) {
}

void Object::wait(long long timeout, int nanos) {
}

void Object::wait() {
}

void Object::finalize() {
}

/*** Class ***/
Class::Class(const char * name) {
    this->name = name;
}

bool Class::isAssignableFrom(Class * type) {
    return false;
}

