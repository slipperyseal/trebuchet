package net.catchpole.trebuchet.profile;

import net.catchpole.trebuchet.code.CodeWriter;
import spoon.reflect.declaration.CtType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SourceShovel {
    private String projectName;
    private CodeWriter headerWriter;
    private CodeWriter codeWriter;
    private TypeMapper typeMapper = new TypeMapper();
    private List<CtType<?>> ctTypeList;
    private List<ClassShovel> classShovelList = new ArrayList<ClassShovel>();

    public SourceShovel(String projectName, CodeWriter headerWriter, CodeWriter codeWriter) {
        this.projectName = projectName;
        this.headerWriter = headerWriter;
        this.codeWriter = codeWriter;
    }

    public void addClasses(List<CtType<?>> ctTypeList) {
        this.ctTypeList = ctTypeList;
    }

    public void generate() {
        this.codeWriter.println();
        this.codeWriter.print("#include \"");
        this.codeWriter.print(projectName);
        this.codeWriter.println(".h\"");
        this.codeWriter.println();

        for (CtType ctType : ctTypeList) {
            ClassShovel classShovel = new ClassShovel(ctType, headerWriter, codeWriter, typeMapper);
            classShovelList.add(classShovel);
        }

        orderDependencies();
        addForwardDepenedencies();
        headerWriter.println();

        for (ClassShovel classShovel : classShovelList) {
            classShovel.process();
        }
    }

    private void addForwardDepenedencies() {
        for (ClassShovel classShovel : classShovelList) {
            headerWriter.print("class ");
            headerWriter.print(classShovel.getName());
            headerWriter.println(";");
        }
    }

    private void orderDependencies() {
        Collections.sort(classShovelList, new Comparator<ClassShovel>() {
            @Override
            public int compare(ClassShovel o1, ClassShovel o2) {
                return o1.inheritsFrom(o2.getCtType()) ? 1 : -1;
            }
        });
    }
}
