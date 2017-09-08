
#ifndef UNIVERSE_H
#define UNIVERSE_H

class Scanner;
class Device;
class LongRangeScanner;
class TowingVessel;
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

private:
    /*** trebuchet.equipment.LongRangeScanner$SubspaceScanner ***/
    class SubspaceScanner: public Scanner {
    private:
        long long subspaceFrequency;
    
    public:
        SubspaceScanner();
        void scan();
    
    };
    
    /*** trebuchet.equipment.LongRangeScanner$TachyonScanner ***/
    class TachyonScanner: public Scanner {
    private:
        long long tachyonDopplerCalibration;
    
    public:
        TachyonScanner();
        void scan();
    
    };
    
};

/*** trebuchet.craft.TowingVessel ***/
class TowingVessel {
public:
    static int REGISTRATION;
private:
    // We set down on LV-426
    const char * name;
    short starfreighterClass;
    LongRangeScanner * longRangeScanner;

public:
    TowingVessel();
    TowingVessel(LongRangeScanner * longRangeScanner);
    LongRangeScanner * getLongRangeScanner();
    void reset(LongRangeScanner * longRangeScanner, short starfreighterClass);
    const char * getName();

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
