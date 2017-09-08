
#include "universe.h"


/*** trebuchet.equipment.Scanner ***/
void Scanner::scan() {
}

/*** trebuchet.equipment.Device ***/
Device::Device() {
}

void Device::explode() {
}

/*** trebuchet.equipment.LongRangeScanner$SubspaceScanner ***/
LongRangeScanner::SubspaceScanner::SubspaceScanner() {
    this->subspaceFrequency = 10483298;
}

void LongRangeScanner::SubspaceScanner::scan() {
    this->subspaceFrequency = this->subspaceFrequency + 2;
}

/*** trebuchet.equipment.LongRangeScanner$TachyonScanner ***/
LongRangeScanner::TachyonScanner::TachyonScanner() {
    this->tachyonDopplerCalibration = 22340;
}

void LongRangeScanner::TachyonScanner::scan() {
    this->tachyonDopplerCalibration = this->tachyonDopplerCalibration + 4;
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
}

int LongRangeScanner::getDeviceId() {
    return 1337;
}

/*
The USCSS Nostromo (reg. 180924609) was a $42 million (and adjusted) commercial
towing vessel owned by the Weyland-Yutani Corporation, a Lockmart CM 88B
Bison M-Class starfreighter used as a commercial hauler between Thedus and Earth.
*/
/*** trebuchet.craft.TowingVessel ***/
int TowingVessel::REGISTRATION = 180924609;

TowingVessel::TowingVessel() {
    this->name = "Nostromo";
    this->starfreighterClass = 'M';
    
}

TowingVessel::TowingVessel(LongRangeScanner * longRangeScanner) {
    this->name = "Nostromo";
    this->starfreighterClass = 'M';
    
    this->longRangeScanner = longRangeScanner;
}

LongRangeScanner * TowingVessel::getLongRangeScanner() {
    return this->longRangeScanner;
}

void TowingVessel::reset(LongRangeScanner * longRangeScanner, short starfreighterClass) {
    // Don't call this method when above light speed.
    this->longRangeScanner = longRangeScanner;
    this->starfreighterClass = starfreighterClass;
}

const char * TowingVessel::getName() {
    return this->name;
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
    // return (this == obj);
    return false;
}

Object * Object::clone() {
    return 0;
}

const char * Object::toString() {
    // return getClass().getName() + "@" + Integer.toHexString(hashCode());
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

