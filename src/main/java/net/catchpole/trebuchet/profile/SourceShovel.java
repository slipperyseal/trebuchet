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

        String projectHeader = projectName.toUpperCase() + "_H";
        headerWriter.print("#ifndef ");
        headerWriter.println(projectHeader);
        headerWriter.print("#define ");
        headerWriter.println(projectHeader);
        headerWriter.println();

        orderDependencies();
        addForwardDepenedencies();

        headerWriter.println();

        for (ClassShovel classShovel : classShovelList) {
            classShovel.addClass();
        }

        headerWriter.println("#endif");
    }

    private void addForwardDepenedencies() {
        for (ClassShovel classShovel : classShovelList) {
            headerWriter.print("class ");
            headerWriter.print(classShovel.getName());
            headerWriter.println(";");
        }

//        for (ClassShovel classShovel : classShovelList) {
//            codeWriter.print("Class class");
//            codeWriter.print(classShovel.getName());
//            codeWriter.print(" = Class(\"");
//            codeWriter.print(classShovel.getName());
//            codeWriter.println("\");");
//
//            codeWriter.print("Class * super");
//            codeWriter.print(classShovel.getName());
//            codeWriter.print("[] = {");
//            FirstPrintOptions firstPrintOptions = new FirstPrintOptions(codeWriter, null, ",");
//            for (ClassShovel testShovel : classShovelList) {
//                if (classShovel.inheritsFrom(testShovel.getCtType())) {
//                    firstPrintOptions.print();
//                    codeWriter.print("&class");
//                    codeWriter.print(testShovel.getName());
//                }
//            }
//            codeWriter.println("};");
//        }
        codeWriter.println();

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
