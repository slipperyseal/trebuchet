package net.catchpole.trebuchet.code;

public class FirstPrintOptions {
    private CodeWriter codeWriter;
    private String onFirst;
    private String remaining;
    private boolean trip = false;

    public FirstPrintOptions(CodeWriter codeWriter, String onFirst, String remaining) {
        this.codeWriter = codeWriter;
        this.onFirst = onFirst;
        this.remaining = remaining;
    }

    public void print() {
        if (!trip) {
            if (onFirst != null) {
                codeWriter.print(onFirst);
            }
            trip = true;
        } else {
            if (remaining != null) {
                codeWriter.print(remaining);
            }
        }
    }
}
