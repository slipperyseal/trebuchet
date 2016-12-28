
#include "universe.h"

/*** trebuchet.equipment.Scanner ***/
void Scanner::scan() {
}

/*** trebuchet.equipment.Device ***/
Device::Device() {
}

void Device::expode() {
}

/*** trebuchet.equipment.LongRangeScanner ***/
LongRangeScanner::LongRangeScanner() {
}

int LongRangeScanner::getRange() {
    return 10;
}

void LongRangeScanner::setRange(int range) {
}

void LongRangeScanner::scan() {
}

int LongRangeScanner::getDeviceId() {
    return 1;
}

/*** trebuchet.craft.Spaceship ***/
int Spaceship::FIRSTSHIP = 100;

Spaceship::Spaceship(LongRangeScanner * longRangeScanner) {
    this->serialNumber = 120;
}

LongRangeScanner * Spaceship::getLongRangeScanner() {
    return 0;
}

void Spaceship::reset(LongRangeScanner * longRangeScanner, int serialNumber) {
}

/*** trebuchet.Universe ***/
Universe::Universe() {
}

void Universe::main(char ** args) {
}

int main(int argc, char* argv[]) {
    Universe::main(0);
    return 0;
}

