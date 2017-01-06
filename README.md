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
features necessary to do that for arbitrary programs.

C++ was chosen as the target language as Java and C++ have enough in common for a reasonably logical translation.

Language transcoders often include a support library to implement language features, garbage collection etc.
Our approach is to have supply the leanest possible support code but then allow optional extensions for mode advanced features.
This should assist when integrating generated code into various platforms. You may wish to integrate with your own platform's
memory management.

### Development Roadmap

![#33ff15](http://placehold.it/15/33ff15/000000?text=+) Implemented features. ![#1589F0](http://placehold.it/15/1589F0/000000?text=+) High Priority features. ![#c5f015](http://placehold.it/15/c5f015/000000?text=+) Lower priority features. ![#f03c15](http://placehold.it/15/f03c15/000000?text=+) Maybe never. Use a JVM?

- ![#33ff15](http://placehold.it/15/33ff15/000000?text=+) `Bean` Parameter Assignment, Return Values, Constructors
- ![#33ff15](http://placehold.it/15/33ff15/000000?text=+) Simple Arithmetic (some working cases)
- ![#33ff15](http://placehold.it/15/33ff15/000000?text=+) Interfaces, Abstract methods
- ![#33ff15](http://placehold.it/15/33ff15/000000?text=+) Static Initializers, Class Field Initializers
- ![#33ff15](http://placehold.it/15/33ff15/000000?text=+) Incrementing heap allocator with no garbage collection
- ![#1589F0](http://placehold.it/15/1589F0/000000?text=+) `instanceof` operator
- ![#1589F0](http://placehold.it/15/1589F0/000000?text=+) Common java.lang.Object and java.lang.Class methods
- ![#1589F0](http://placehold.it/15/1589F0/000000?text=+) Exceptions
- ![#1589F0](http://placehold.it/15/1589F0/000000?text=+) Generics handling (not templates)
- ![#c5f015](http://placehold.it/15/c5f015/000000?text=+) Synchronization, Volatile references, Threads
- ![#c5f015](http://placehold.it/15/c5f015/000000?text=+) Alternate Memory Managment
- ![#c5f015](http://placehold.it/15/c5f015/000000?text=+) Custom Collections Implementations
- ![#f03c15](http://placehold.it/15/f03c15/000000?text=+) Reflection, Bytecode Class Loading

### Optimisations

Modern C++ compilers can produce some amazing optimizations. We will rely on this to do most of the heavy lifting.
Yet there are some quick wins we can apply in the translation process to help the C++ compiler to optimize optimally.
Examples..

- ![#1589F0](http://placehold.it/15/1589F0/000000?text=+) As all Java methods are virtual, demoting virtual methods to non-virtual where no type-cast can occur.
- ![#1589F0](http://placehold.it/15/1589F0/000000?text=+) Demote heap allocated objects to local scope where reference doesn't escape scope.
- ![#1589F0](http://placehold.it/15/1589F0/000000?text=+) Convert regular getters and setters to direct field access.
- ![#1589F0](http://placehold.it/15/1589F0/000000?text=+) Disable index and type safety checks when logically safe to do so.
- ![#1589F0](http://placehold.it/15/1589F0/000000?text=+) Use of different memory pools or memory managment by type.

### Example Translation

Trebuchet will scan a source directory for Java source files, generating single CPP and Header files.
Class re-ordering needs to occur to support C++'s forward declaration requirements for inheritated classes.
The example source generates `universe.cpp` and `universe.h`.
These can then be compiled with `trebuchet.cpp` which replaces the new operator and will provide support functions.

The following test case demonstrates a simple code structure featuring interfaces, abstract methods,
`Bean` assignment and return values etc...

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
