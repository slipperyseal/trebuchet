package net.catchpole.trebuchet.profile;

import net.catchpole.trebuchet.code.ChangeTracker;
import net.catchpole.trebuchet.code.CodeWriter;
import net.catchpole.trebuchet.code.FirstPrintOptions;
import net.catchpole.trebuchet.spoon.MatchAllFilter;
import net.catchpole.trebuchet.spoon.MatchTypeFilter;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtPackageReference;
import spoon.reflect.reference.CtParameterReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtBlockImpl;

import java.util.List;

public class ClassShovel {
    private CtType ctType;
    private TypeMapper typeMapper;
    private String name;
    private boolean isInterface;
    private ChangeTracker<ModifierKind> visibityChange = new ChangeTracker<ModifierKind>(ModifierKind.PUBLIC);

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

        headerWriter.print("/*** ");
        headerWriter.print(ctType.getQualifiedName());
        headerWriter.println(" ***/");

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

        for (CtElement ctElement : ctConstructor.getElements(new MatchTypeFilter<CtElement>(CtExecutable.class))) {
            System.out.println(ctConstructor.getSimpleName());
            addExecutableBlock((CtExecutable) ctElement);
            System.out.println();
        }

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

        for (CtElement ctElement : ctMethod.getElements(new MatchTypeFilter<CtElement>(CtExecutable.class))) {
            System.out.println(ctMethod.getSimpleName());
            addExecutableBlock((CtExecutable) ctElement);
            System.out.println();
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

    private void addExecutableBlock(CtExecutable ctExecutable) {
        for (CtElement blockElement : ctExecutable.getElements(new MatchTypeFilter<CtElement>(CtBlockImpl.class))) {
            for (CtElement ctElement : blockElement.getElements(new MatchAllFilter<CtElement>())) {
                System.out.println(ctElement.getClass().getSimpleName() + " " + ctElement.toString());
                addElement(ctElement);
            }
        }
    }

    private String deferred;

    private boolean doDeferred() {
        if (deferred != null) {
            codeWriter.print(deferred);
            deferred = null;
            return true;
        }
        return false;
    }

    private void addElement(CtElement ctElement) {
        if (ctElement instanceof CtReturn) {
            codeWriter.print("return ");
        }
        if (ctElement instanceof CtLiteral) {
            CtLiteral ctLiteral = (CtLiteral)ctElement;
            codeWriter.print(ctLiteral.getValue());
            codeWriter.println(';');
        }
        if (ctElement instanceof CtFieldReference) {
            CtFieldReference ctFieldReference = (CtFieldReference)ctElement;
            codeWriter.print(ctFieldReference.getSimpleName());
            if (!doDeferred()) {
                codeWriter.println(';');
            }
        }
        if (ctElement instanceof CtFieldWrite) {
            CtFieldWrite ctFieldWrite = (CtFieldWrite)ctElement;
            deferred = " = ";
        }
        if (ctElement instanceof CtThisAccess) {
            CtThisAccess ctThisAccess = (CtThisAccess)ctElement;
            codeWriter.print("this->");
        }
        if (ctElement instanceof CtTypeReference) {
            CtTypeReference ctTypeReference = (CtTypeReference)ctElement;
        }
        if (ctElement instanceof CtPackageReference) {
            CtPackageReference ctPackageReference = (CtPackageReference)ctElement;

        }
        if (ctElement instanceof CtTypeAccess) {
            CtTypeAccess ctTypeAccess = (CtTypeAccess)ctElement;
        }
        if (ctElement instanceof CtParameterReference) {
            CtParameterReference ctParameterReference = (CtParameterReference)ctElement;
            codeWriter.print(ctParameterReference.getSimpleName());
            codeWriter.println(';');
        }
    }

    private void addVisibility(ModifierKind modifierKind) {
        ModifierKind printModifier = visibityChange.changedValue(modifierKind);
        if (printModifier != null) {
            headerWriter.print(printModifier);
            headerWriter.println(':');
        }
    }

    public String toString() {
        return name;
    }
}
