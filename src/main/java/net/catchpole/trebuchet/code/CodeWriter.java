package net.catchpole.trebuchet.code;

public interface CodeWriter {
    void print(boolean b);

    void print(char c);

    void print(int i);

    void print(long l);

    void print(float v);

    void print(double v);

    void print(char[] chars);

    void print(java.lang.String s);

    void print(java.lang.Object o);

    void println();

    void println(boolean b);

    void println(char c);

    void println(int i);

    void println(long l);

    void println(float v);

    void println(double v);

    void println(char[] chars);

    void println(java.lang.Object o);

    void indent();

    void outdent();
}
