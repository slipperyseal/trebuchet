package net.catchpole.trebuchet.code;

import java.io.ByteArrayOutputStream;

public class BufferedCodeWriter extends OutputStreamCodeWriter {
    public BufferedCodeWriter() {
        super(new ByteArrayOutputStream());
    }

    public String toString() {
        super.close();
        return ((ByteArrayOutputStream)getOutputStream()).toString();
    }
}
