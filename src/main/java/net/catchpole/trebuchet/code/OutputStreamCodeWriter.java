package net.catchpole.trebuchet.code;

import java.io.OutputStream;
import java.io.PrintWriter;

public class OutputStreamCodeWriter implements CodeWriter {
    private final OutputStream outputStream;
    private final PrintWriter printWriter;
    private int indent = 0;
    private boolean startOfLine = true;

    public OutputStreamCodeWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.printWriter = new PrintWriter(outputStream);
    }

    public void close() {
        printWriter.close();
    }

    public void print(boolean b) {
        checkIndent(false);
        printWriter.print(b);
    }

    public void print(char c) {
        checkIndent(false);
        printWriter.print(c);
    }

    public void print(int i) {
        checkIndent(false);
        printWriter.print(i);
    }

    public void print(long l) {
        checkIndent(false);
        printWriter.print(l);
    }

    public void print(float v) {
        checkIndent(false);
        printWriter.print(v);
    }

    public void print(double v) {
        checkIndent(false);
        printWriter.print(v);
    }

    public void print(char[] chars) {
        checkIndent(false);
        printWriter.print(chars);
    }

    public void print(String s) {
        checkIndent(false);
        printWriter.print(s);
    }

    public void print(Object o) {
        checkIndent(false);
        printWriter.print(o);
    }

    public void println() {
        checkIndent(true);
        printWriter.println();
    }

    public void println(boolean b) {
        checkIndent(true);
        printWriter.println(b);
    }

    public void println(char c) {
        checkIndent(true);
        printWriter.println(c);
    }

    public void println(int i) {
        checkIndent(true);
        printWriter.println(i);
    }

    public void println(long l) {
        checkIndent(true);
        printWriter.println(l);
    }

    public void println(float v) {
        checkIndent(true);
        printWriter.println(v);
    }

    public void println(double v) {
        checkIndent(true);
        printWriter.println(v);
    }

    public void println(char[] chars) {
        checkIndent(true);
        printWriter.println(chars);
    }

    public void println(String s) {
        checkIndent(true);
        printWriter.println(s);
    }

    public void println(Object o) {
        checkIndent(true);
        printWriter.println(o);
    }

    public void indent() {
        indent++;
    }

    public void outdent() {
        if (indent-- < 0) {
            indent = 0;
        }
    }

    private void checkIndent(boolean println) {
        if (startOfLine) {
            for (int x = 0; x < indent; x++) {
                printWriter.print("    ");
            }
            startOfLine = false;
        }
        if (println) {
            startOfLine = true;
        }
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
