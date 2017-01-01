
class Scanner;
class Device;
class LongRangeScanner;
class Spaceship;
class Universe;

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
    char * name;
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
    static void main(char ** args);
};

