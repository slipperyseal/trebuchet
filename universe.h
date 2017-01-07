
#ifndef UNIVERSE_H
#define UNIVERSE_H

class Scanner;
class Device;
class LongRangeScanner;
class Spaceship;
class Universe;
class Object;
class Class;

/*** trebuchet.equipment.Scanner ***/
class Scanner {
public:
    virtual void scan() = 0;
};

/*** trebuchet.equipment.Device ***/
class Device {
public:
    Device();
    void explode();
    virtual int getDeviceId() = 0;
};

/*** trebuchet.equipment.LongRangeScanner ***/
class LongRangeScanner: public Device, public Scanner {
private:
    int range;

public:
    LongRangeScanner();
    int getRange();
    void setRange(int range);
    void scan();
    int getDeviceId();
};

/*** trebuchet.craft.Spaceship ***/
class Spaceship {
public:
    static int FIRSTSHIP;
private:
    const char * name;
    int serialNumber;
    LongRangeScanner * longRangeScanner;

public:
    Spaceship(LongRangeScanner * longRangeScanner);
    LongRangeScanner * getLongRangeScanner();
    void reset(LongRangeScanner * longRangeScanner, int serialNumber);
};

/*** trebuchet.Universe ***/
class Universe {
public:
    Universe();
    static void main(char * args);
};

/*** java.lang.Object ***/
class Object {
public:
    Object();
    Object * getClass();
    int hashCode();
    bool equals(Object * obj);
protected:
    Object * clone();
public:
    const char * toString();
    void notify();
    void notifyAll();
    void wait(long long timeout);
    void wait(long long timeout, int nanos);
    void wait();
protected:
    void finalize();
};

/*** Class ***/
class Class {
private:
    const char * name;

public:
    Class(const char * name);
    bool isAssignableFrom(Class * type);
};

#endif
