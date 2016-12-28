package net.catchpole.trebuchet.profile;

import net.catchpole.trebuchet.code.CodeWriter;
import net.catchpole.trebuchet.code.FirstPrintOptions;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.Filter;
import spoon.support.reflect.code.CtBlockImpl;

import java.util.List;

public class ClassShovel {
    private CtType ctType;
    private TypeMapper typeMapper;
    private String name;
    private boolean isInterface;
    private ModifierKind lastVisibity;

    private CodeWriter codeWriter;
    private CodeWriter headerWriter;
    private FieldShovel fieldShovel;

    public ClassShovel(CtType ctType, CodeWriter headerWriter, CodeWriter codeWriter, TypeMapper typeMapper) {
        this.ctType = ctType;
        this.codeWriter = codeWriter;
        this.headerWriter = headerWriter;
        this.typeMapper = typeMapper;

        this.isInterface = ctType instanceof CtInterface;
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

    public String getName() {
        return name;
    }

    public void process() {
        codeWriter.print("/*** ");
        codeWriter.print(ctType.getQualifiedName());
        codeWriter.println(" ***/");

        addClassSignature(ctType);
        headerWriter.println(" {");

        this.fieldShovel = new FieldShovel(ctType);

        for (CtField ctField : fieldShovel.getAllFields()) {
            addField(ctField);
        }

        if (!fieldShovel.getAllFields().isEmpty()) {
            headerWriter.println();
        }

        boolean containsMain = false;
        for (CtTypeMember ctTypeMember : (List<CtTypeMember>)ctType.getTypeMembers()) {
            if (ctTypeMember instanceof CtConstructor && !isInterface) {
                addConstuctorSignature((CtConstructor)ctTypeMember);
                headerWriter.println(";");
                headerWriter.outdent();
            }
            if (ctTypeMember instanceof CtMethod) {
                addMethodSignature((CtMethod) ctTypeMember);
                headerWriter.println(";");
                headerWriter.outdent();

                CtMethod ctMethod = (CtMethod)ctTypeMember;
                if (ctMethod.getModifiers().contains(ModifierKind.STATIC) &&
                        ctMethod.getModifiers().contains(ModifierKind.PUBLIC) &&
                        ctMethod.getSimpleName().equals("main")) {
                    containsMain = true;
                }
            }
        }

        headerWriter.println("};");
        headerWriter.println();

        if (!fieldShovel.getStaticInitializedFields().isEmpty()) {
            addStaticInitializers(fieldShovel);
        }

        for (CtTypeMember ctTypeMember : (List<CtTypeMember>)ctType.getTypeMembers()) {
            if (ctTypeMember instanceof CtConstructor && !isInterface) {
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
        String name = typeMapper.getTypeName(ctType);
        codeWriter.print(name);
        codeWriter.print("::");
        codeWriter.print(name);
        addParameters(codeWriter, ctConstructor);

        codeWriter.println(" {");
        codeWriter.indent();
        addFieldInitializers();
        codeWriter.outdent();
        codeWriter.println("}");
        codeWriter.println();
    }

    private void addMethod(CtMethod ctMethod) {
        String returnType = typeMapper.getTypeReference(ctMethod.getType());
        codeWriter.print(returnType);
        codeWriter.print(' ');
        codeWriter.print(typeMapper.getTypeName(ctType));
        codeWriter.print("::");
        codeWriter.print(ctMethod.getSimpleName());
        addParameters(codeWriter, ctMethod);

        codeWriter.println(" {");
        codeWriter.indent();
        if (!returnType.equals("void")) {
            for (CtElement ctElement : ctMethod.getElements(new Filter<CtElement>() {
                @Override
                public boolean matches(CtElement ctElement) {
                    return ctElement instanceof CtExecutable;
                }
            })) {
                addExecutable((CtExecutable)ctElement);
            }
        }
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

    private void addStaticInitializers(FieldShovel fieldShovel) {
        for (CtField ctField : fieldShovel.getStaticInitializedFields()) {
            codeWriter.print(typeMapper.getTypeName(ctField.getType()));
            codeWriter.print(' ');
            codeWriter.print(typeMapper.getTypeName(ctField.getDeclaringType()));
            codeWriter.print("::");
            codeWriter.print(ctField.getSimpleName());
            codeWriter.print(" = ");
            for (CtLiteral ctLiteral : fieldShovel.getFieldInitializer(ctField)) {
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
        for (CtField ctField : fieldShovel.getMemberInitializedFields()) {
            codeWriter.print("this->");
            codeWriter.print(ctField.getSimpleName());
            codeWriter.print(" = ");
            for (CtLiteral ctLiteral : fieldShovel.getFieldInitializer(ctField)) {
                codeWriter.print(ctLiteral);
            }
            codeWriter.println(';');
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
        boolean isVirtual = isInterface || ctMethod.getModifiers().contains(ModifierKind.ABSTRACT);
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

    private void addExecutable(CtExecutable ctExecutable) {
        boolean result = false;
        for (CtElement ctElement : ctExecutable.getElements(new Filter<CtElement>() {
            @Override
            public boolean matches(CtElement ctElement) {
                return ctElement instanceof CtBlockImpl;
            }
        })) {

            for (CtElement ctElement2 : ctElement.getElements(new Filter<CtElement>() {
                @Override
                public boolean matches(CtElement ctElement) {
                    return true;
                }
            })) {
                if (ctElement2 instanceof CtLiteral) {
                    codeWriter.print("return ");
                    codeWriter.print(ctElement2.toString());
                    codeWriter.println(";");
                    result = true;
                }
            }
        }
        if (!result) {
            codeWriter.println("return 0;");
        }
    }

    private void addVisibility(ModifierKind modifierKind) {
        if (modifierKind != null && lastVisibity != null && modifierKind.equals(lastVisibity)) {
            return;
        }
        if (modifierKind == null) {
            modifierKind = ModifierKind.PUBLIC;
        }
        headerWriter.print(modifierKind);
        headerWriter.println(':');
        this.lastVisibity = modifierKind;
    }

    public String toString() {
        return name;
    }
}
