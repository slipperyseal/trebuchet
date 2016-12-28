package net.catchpole.trebuchet;

import net.catchpole.trebuchet.code.OutputStreamCodeWriter;
import net.catchpole.trebuchet.profile.SourceShovel;
import spoon.Launcher;
import spoon.reflect.factory.Factory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Trebuchet {
    public static void main(String[] args) throws IOException {
        Trebuchet trebuchet = new Trebuchet();
        trebuchet.process(args[0], args[1]);
    }

    public void process(String projectName, String path) throws IOException {
        FileOutputStream codeOutputStream = new FileOutputStream(projectName + ".cpp");
        try {
            FileOutputStream headerOutputStream = new FileOutputStream(projectName + ".h");
            try {
                this.process(projectName, path, headerOutputStream, codeOutputStream);
            } finally {
                headerOutputStream.close();
            }
        } finally {
            codeOutputStream.close();
        }
    }

    public void process(String projectName, String path, OutputStream headerOutputStream, OutputStream codeOutputStream) {
        Launcher launcher = new Launcher();
        final String[] args = { "--input", path, "--output-type", "nooutput" };
        launcher.setArgs(args);
        launcher.run();

        OutputStreamCodeWriter headerCodeWriter = new OutputStreamCodeWriter(headerOutputStream);
        OutputStreamCodeWriter codeCodeWriter = new OutputStreamCodeWriter(codeOutputStream);
        SourceShovel sourceShovel = new SourceShovel(projectName, headerCodeWriter, codeCodeWriter);

        headerCodeWriter.println();

        Factory factory = launcher.getFactory();
        sourceShovel.addClasses(factory.Class().getAll());
        sourceShovel.generate();

        headerCodeWriter.close();
        codeCodeWriter.close();
    }
}
