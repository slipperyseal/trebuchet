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
Or your boss has asked you to port some terrible Java code to some rubbish device.
Pop the code into Trebuchet and pull the handle.

The aim of the project is not to replace a Java Virtual Machine and this project may never implement all of Java's
features necessary to do that for arbitary programs.

C++ was chosen as the target language as Java and C++ have enough in common for a reasonably logical translation.
The output C++ isn't meant to be read and maintained in it's own right but we'll certainly try to make it as well formed as we can.

### Current status

The project is quite young but is currently generating class headers and simple getters and setters for our specific test case.

### Optimisations

Modern C++ compilers produce amazing optimizations. So for the most part we will rely on this to do most of the heavy lifting.
Yet there are some quick wins we can apply in the translation process to allow the C++ compiler to optimize optimally.

Examples..

* As all Java methods are virtual, demoting virtual methods to non-virtual where no type-cast occurs.
* Demote heap allocated objects to local scope objects where reference doesn't escape scope.

### Development Schedule

Not all of Java features suit transation to C++. This should give you an idea of priority for each feature.

##### Mandatory
* Assignment, Flow control
* Incrementing heap allocator with no garbage collection

##### High Priority
* Exceptions
* Generics need not be implemented as templates. Type erasure should be able to be converted to simple type casts.
* Generational garbage collection (Manual GC runs at the end of a scope)

##### Medium Priority
* Synchronization, Volatile references, Threads
* Native implemenations of file and socket IO. 
* Alternate implemenations of common Java Collections classes which suit native code

##### Low Priority (Maybe Never. Use a JVM)
* Reflection
* Bytecode class loading

### Example Translation

This demonstrates the output C++ headers and code from the classes of our test case.

#### Java input

        public class Spaceship {
        	public static final int FIRSTSHIP = 100;
        	private String name = "Nostromo";
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
            public void explode() {
            }

            public abstract int getDeviceId();
        }

        public class LongRangeScanner extends Device implements Scanner {
            private int range;

            public int getRange() {
                return this.range;
            }

            public void setRange(int range) {
                this.range = range;
            }

            public void scan() {
                range = range + 2;
                range = range - 1;
            }

            public int getDeviceId() {
                return 1337;
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
            static void main(char ** * args);
        };

#### C++ code output

        /*** trebuchet.equipment.Scanner ***/
        void Scanner::scan() {
        }

        /*** trebuchet.equipment.Device ***/
        Device::Device() {
        }

        void Device::explode() {
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
            this->range = this->range + 2;
            this->range = this->range - 1;
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

        void Universe::main(char ** * args) {
            ;
        }

        int main(int argc, char* argv[]) {
            Universe::main(0);
            return 0;
        }
