
package java.lang;

public class Object {
//    public final Class<?> getClass() {
    public final Object getClass() {
        return null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object obj) {
        //return (this == obj);
        return false;
    }

    protected Object clone() throws CloneNotSupportedException {
        return null;
    }
    
    public String toString() {
        return null;
        //return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
    
    public final void notify() { }

    public final void notifyAll() { }
    
    public final void wait(long timeout) throws InterruptedException { }

    public final void wait(long timeout, int nanos) throws InterruptedException { }

    public final void wait() throws InterruptedException { }

    protected void finalize() throws Throwable { }
}
