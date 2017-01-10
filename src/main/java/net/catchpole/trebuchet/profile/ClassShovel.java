package net.catchpole.trebuchet.profile;

import net.catchpole.trebuchet.code.ChangeTracker;
import net.catchpole.trebuchet.code.CodeWriter;
import net.catchpole.trebuchet.code.FirstPrintOptions;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtTypeReference;

import java.util.List;
import java.util.Set;

public class ClassShovel {
    private CtType ctType;
    private CodeWriter codeWriter;
    private CodeWriter headerWriter;
    private TypeMapper typeMapper;
    private FieldGroups fieldGroups;
    private String name;

    private ChangeTracker<ModifierKind> visibityChange = new ChangeTracker<ModifierKind>(ModifierKind.PUBLIC);

    public ClassShovel(CtType ctType, CodeWriter headerWriter, CodeWriter codeWriter, TypeMapper typeMapper) {
        this.ctType = ctType;
        this.codeWriter = codeWriter;
        this.headerWriter = headerWriter;
        this.typeMapper = typeMapper;
        this.fieldGroups = new FieldGroups(ctType);
        this.name = typeMapper.getTypeName(ctType);
    }

    public CtType getCtType() {
        return ctType;
    }

    public boolean inheritsFrom(CtType testType) {
        CtTypeReference testReference = testType.getReference();
        CtTypeReference ctTypeReference = ctType.getReference();

        while ((ctTypeReference = ctTypeReference.getSuperclass()) != null) {
            if (ctTypeReference.equals(testReference)) {
                return true;
            }
        }

        for (CtTypeReference<?> ctInterface : ctType.getReference().getSuperInterfaces()) {
            if (ctInterface.equals(testReference)) {
                return true;
            }
        }
        return false;
    }

    public void addClass() {
        headerWriter.print("/*** ");
        headerWriter.print(ctType.getQualifiedName());
        headerWriter.println(" ***/");

        addClassSignature(ctType);
        headerWriter.println(" {");

        for (CtField ctField : fieldGroups.getAllFields()) {
            addField(ctField);
        }

        if (!fieldGroups.getAllFields().isEmpty()) {
            headerWriter.println();
        }

        boolean containsMain = false;
        for (CtTypeMember ctTypeMember : (List<CtTypeMember>)ctType.getTypeMembers()) {
            if (ctTypeMember instanceof CtConstructor && !(ctType instanceof CtInterface)) {
                addConstuctorSignature((CtConstructor)ctTypeMember);
                headerWriter.println(";");
                headerWriter.outdent();
            }
            if (ctTypeMember instanceof CtMethod) {
                addMethodSignature((CtMethod) ctTypeMember);
                headerWriter.println(";");
                headerWriter.outdent();

                CtMethod ctMethod = (CtMethod)ctTypeMember;
                if (ctType.getDeclaringType() == null &&
                        ctMethod.getModifiers().contains(ModifierKind.STATIC) &&
                        ctMethod.getModifiers().contains(ModifierKind.PUBLIC) &&
                        ctMethod.getSimpleName().equals("main")) {
                    containsMain = true;
                }
            }
        }

        headerWriter.println();
        for (CtType innerClass : (Set<CtType>)ctType.getNestedTypes()) {
            addVisibility(ModifierKind.PRIVATE);
            headerWriter.indent();
            ClassShovel innerShovel = new ClassShovel(innerClass, headerWriter, codeWriter, typeMapper);
            innerShovel.addClass();
            headerWriter.outdent();
        }

        headerWriter.println("};");
        headerWriter.println();

        codeWriter.print("/*** ");
        codeWriter.print(ctType.getQualifiedName());
        codeWriter.println(" ***/");

        if (!fieldGroups.getStaticInitializedFields().isEmpty()) {
            addStaticInitializers(fieldGroups);
        }

        for (CtTypeMember ctTypeMember : (List<CtTypeMember>)ctType.getTypeMembers()) {
            if (ctTypeMember instanceof CtConstructor && !(ctType instanceof CtInterface)) {
                addConstructor((CtConstructor)ctTypeMember);
            }
            if (ctTypeMember instanceof CtMethod) {
                CtMethod ctMethod = (CtMethod) ctTypeMember;
                if (!ctMethod.getModifiers().contains(ModifierKind.ABSTRACT)) {
                    addMethod(ctMethod);
                }
            }
        }

        if (containsMain) {
            addMainMethod();
        }
    }

    private void addMainMethod() {
        codeWriter.println("int main(int argc, char* argv[]) {");
        codeWriter.indent();
        codeWriter.print(name);
        codeWriter.println("::main(0);");
        codeWriter.println("return 0;");
        codeWriter.outdent();
        codeWriter.println("}");
        codeWriter.println();
    }

    private void addConstructor(CtConstructor ctConstructor) {
        CtType outterType = ctType.getDeclaringType();
        if (outterType != null) {
            codeWriter.print(typeMapper.getTypeName(outterType));
            codeWriter.print("::");
        }
        codeWriter.print(name);
        codeWriter.print("::");
        codeWriter.print(name);
        addParameters(codeWriter, ctConstructor);

        codeWriter.println(" {");
        codeWriter.indent();
        addFieldInitializers();

        addBlock(ctConstructor.getBody());

        codeWriter.outdent();
        codeWriter.println("}");
        codeWriter.println();
    }

    private void addMethod(CtMethod ctMethod) {
        String returnType = typeMapper.getTypeReference(ctMethod.getType());
        codeWriter.print(returnType);
        codeWriter.print(' ');

        CtType outterType = ctType.getDeclaringType();
        if (outterType != null) {
            codeWriter.print(typeMapper.getTypeName(outterType));
            codeWriter.print("::");
        }
        codeWriter.print(name);
        codeWriter.print("::");
        codeWriter.print(ctMethod.getSimpleName());
        addParameters(codeWriter, ctMethod);

        codeWriter.println(" {");
        codeWriter.indent();

        addBlock(ctMethod.getBody());

        codeWriter.outdent();
        codeWriter.println("}");
        codeWriter.println();
    }

    private void addClassSignature(CtType ctType) {
        headerWriter.print("class ");
        headerWriter.print(name);

        FirstPrintOptions firstPrintOptions = new FirstPrintOptions(headerWriter, ":", ",");
        CtTypeReference ctTypeReferenceSuper = ctType.getSuperclass();
        if (ctTypeReferenceSuper != null) {
            firstPrintOptions.print();
            headerWriter.print(" public ");
            headerWriter.print(typeMapper.getTypeName(ctTypeReferenceSuper));
        }
        for (CtTypeReference ctTypeReference : ctType.getSuperInterfaces()) {
            firstPrintOptions.print();
            headerWriter.print(" public ");
            headerWriter.print(typeMapper.getTypeName(ctTypeReference));
        }
    }

    private void addStaticInitializers(FieldGroups fieldGroups) {
        for (CtField ctField : fieldGroups.getStaticInitializedFields()) {
            codeWriter.print(typeMapper.getTypeName(ctField.getType()));
            codeWriter.print(' ');
            codeWriter.print(typeMapper.getTypeName(ctField.getDeclaringType()));
            codeWriter.print("::");
            codeWriter.print(ctField.getSimpleName());
            codeWriter.print(" = ");
            for (CtLiteral ctLiteral : fieldGroups.getFieldInitializer(ctField)) {
                codeWriter.print(ctLiteral);
            }
            codeWriter.println(';');
        }
        codeWriter.println();
    }

    private void addField(CtField ctField) {
        addVisibility(ctField.getVisibility());
        headerWriter.indent();
        if (ctField.getModifiers().contains(ModifierKind.STATIC)) {
            headerWriter.print("static ");
        }
        headerWriter.print(typeMapper.getTypeReference(ctField.getType()));
        headerWriter.print(' ');
        headerWriter.print(ctField.getSimpleName());
        headerWriter.println(';');
        headerWriter.outdent();
    }

    private void addFieldInitializers() {
        for (CtField ctField : fieldGroups.getMemberInitializedFields()) {
            codeWriter.print("this->");
            codeWriter.print(ctField.getSimpleName());
            codeWriter.print(" = ");
            for (CtLiteral ctLiteral : fieldGroups.getFieldInitializer(ctField)) {
                codeWriter.print(ctLiteral);
            }
            codeWriter.println(';');
        }
        if (!fieldGroups.getMemberInitializedFields().isEmpty()) {
            codeWriter.println();
        }
    }

    private void addConstuctorSignature(CtConstructor ctConstructor) {
        addVisibility(ctConstructor.getVisibility());
        headerWriter.indent();
        headerWriter.print(typeMapper.getTypeName((CtClass) ctConstructor.getParent()));
        addParameters(headerWriter, ctConstructor);
    }

    private void addMethodSignature(CtMethod ctMethod) {
        addVisibility(ctMethod.getVisibility());
        headerWriter.indent();
        boolean isVirtual = ctType instanceof CtInterface || ctMethod.getModifiers().contains(ModifierKind.ABSTRACT);
        if (isVirtual) {
            headerWriter.print("virtual ");
        }
        if (ctMethod.getModifiers().contains(ModifierKind.STATIC)) {
            headerWriter.print("static ");
        }
        headerWriter.print(typeMapper.getTypeReference(ctMethod.getType()));
        headerWriter.print(' ');
        headerWriter.print(ctMethod.getSimpleName());
        addParameters(headerWriter, ctMethod);
        if (isVirtual) {
            headerWriter.print(" = 0");
        }
    }

    private void addParameters(CodeWriter codeWriter, CtExecutable ctExecutable) {
        codeWriter.print("(");
        FirstPrintOptions firstPrintOptions = new FirstPrintOptions(codeWriter, null, ", ");
        for (CtParameter ctParameter : (List<CtParameter>)ctExecutable.getParameters()) {
            firstPrintOptions.print();
            codeWriter.print(typeMapper.getTypeReference(ctParameter.getType()));
            codeWriter.print(' ');
            codeWriter.print(ctParameter.getSimpleName());
        }
        codeWriter.print(")");
    }

    public void addBlock(CtBlock ctBlock) {
        BlockShovel blockShovel = new BlockShovel(codeWriter, typeMapper);
        blockShovel.addBlock(ctBlock);
    }

    private void addVisibility(ModifierKind modifierKind) {
        ModifierKind printModifier = visibityChange.changedValue(modifierKind);
        if (printModifier != null) {
            headerWriter.print(printModifier);
            headerWriter.println(':');
        }
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
