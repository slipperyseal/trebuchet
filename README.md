Trebuchet Transcompiler
=====

Trebuchet is a Java to C++ source code translator.

### ZOMG WHY WOULD YOU DO THAT??

For the most part there is no good reason not to run a Java program on a Java Virtual Machine.
Modern JVMs provide many benefits and are really really fast.

But sometimes there is a situation where you might want to statically compile your Java program or some parts of it.
Perhaps you are making a version of your software on a platform which doesn't support a JVM.
Maybe you need to share a data model or a specific algorithm on a device which your Java server application communicates with.
It may be you've written a really small utility in Java and it makes sense that this is distributed as a small native executable.

The aim of the project is not to replace a Java Virtual Machine and this project may never implement all of Java's
features necessary to do that for arbitary programs.

C++ was chosen as the target language as Java and C++ have enough in common for a reasonably logical translation.
The output C++ isn't meant to be read and maintained in it's own right but we'll certainly try to make it as well formed as we can.

### Current status

The project is quite young but is currently generating class headers and simple method stubs for a specific test case.

### Optimisations

Modern C++ compilers produce amazing optimizations. So for the most part we will rely on this to do most of the heavy lifting.
Yet there are some quick wins we can apply in the translation process to allow the C++ compiler to optimize optimally.

Examples..

* As all Java methods are virtual, demoting virtual methods to non-virtual where no type-case invocation occurs.
* Demote heap allocated objects to local scope objects where reference doesn't escape scope.

### Example Translation

#### Java input

        public class Spaceship {
            public static final int FIRSTSHIP = 100;
            private String name;
            private int serialNumber = 120;
            private LongRangeScanner longRangeScanner;

            public Spaceship(LongRangeScanner longRangeScanner) {
                this.longRangeScanner = longRangeScanner;
            }

            public LongRangeScanner getLongRangeScanner() {
                return longRangeScanner;
            }

            public void reset(LongRangeScanner longRangeScanner, int serialNumber) {
                this.longRangeScanner = longRangeScanner;
                this.serialNumber = serialNumber;
            }
        }

        public abstract class Device {
            public void expode() {
            }

            public abstract int getDeviceId();
        }

        public class LongRangeScanner extends Device implements Scanner {
            private int range;

            public int getRange() {
                return 10;
            }

            public void setRange(int range) {
                this.range = range;
            }

            public void scan() {
            }

            public int getDeviceId() {
                return 1;
            }
        }

        public interface Scanner {
            void scan();
        }

#### C++ header output

        class Scanner;
        class Device;
        class LongRangeScanner;
        class Spaceship;
        class Universe;

        class Scanner {
        public:
            virtual void scan() = 0;
        };

        class Device {
        public:
            Device();
            void expode();
            virtual int getDeviceId() = 0;
        };

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

        class Universe {
        public:
            Universe();
            static void main(char ** args);
        };

#### C++ code output

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

