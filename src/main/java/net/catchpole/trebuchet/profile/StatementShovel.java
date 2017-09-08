package net.catchpole.trebuchet.profile;

import net.catchpole.trebuchet.code.CodeWriter;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtPackageReference;
import spoon.reflect.reference.CtParameterReference;
import spoon.reflect.reference.CtTypeReference;

import java.util.List;

public class StatementShovel {
    private static BinaryOperatorMapping binaryOperatorMapping = new BinaryOperatorMapping();

    private CodeWriter codeWriter;
    private TypeMapper typeMapper;

    public StatementShovel(CodeWriter codeWriter, TypeMapper typeMapper) {
        this.codeWriter = codeWriter;
        this.typeMapper = typeMapper;
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

    public void finish() {
        codeWriter.println(";");
    }

    public void addElement(CtElement ctElement) {
        List<CtComment> comments = ctElement.getComments();
        for (CtComment ctComment : comments) {
            new CommentWriter(codeWriter).write(ctComment);
        }

        if (ctElement instanceof CtReturn) {
            codeWriter.print("return ");
        }
        if (ctElement instanceof CtLiteral) {
            CtLiteral ctLiteral = (CtLiteral)ctElement;
            Object value = ctLiteral.getValue();
            if (value == null) {
                value = "0";
            }
            codeWriter.print(value);
        }
        if (ctElement instanceof CtFieldReference) {
            CtFieldReference ctFieldReference = (CtFieldReference)ctElement;
            codeWriter.print(ctFieldReference.getSimpleName());
            doDeferred();
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
        }
        if (ctElement instanceof CtBinaryOperator) {
            CtBinaryOperator ctBinaryOperator = (CtBinaryOperator)ctElement;
            String value = binaryOperatorMapping.getMapping(ctBinaryOperator.getKind());
            deferred = " " + value + " ";
        }
        if (ctElement instanceof CtOperatorAssignment) {
            CtOperatorAssignment ctOperatorAssignment = (CtOperatorAssignment)ctElement;
            String value = binaryOperatorMapping.getMapping(ctOperatorAssignment.getKind());
            codeWriter.print(" " + value + "= ");
        }
        if (ctElement instanceof CtConstructorCall) {
            CtConstructorCall ctConstructorCall = (CtConstructorCall)ctElement;
        }
        if (ctElement instanceof CtBlock) {
            codeWriter.println("{");
            codeWriter.indent();
            BlockShovel blockShovel = new BlockShovel(codeWriter, typeMapper);
            blockShovel.addBlock((CtBlock) ctElement);
            codeWriter.outdent();
            codeWriter.println("}");
        }
        if (ctElement instanceof CtIf) {
            CtIf ctIf = (CtIf)ctElement;
            codeWriter.print("if (");
            codeWriter.print(ctIf.getCondition());
            codeWriter.print(")");
        }
//        if (ctElement instanceof CtExecutableReference) {
//            CtExecutableReference ctExecutableReference = (CtExecutableReference)ctElement;
//            codeWriter.print(ctExecutableReference.getSimpleName());
//            codeWriter.print("()");
//        }
    }
}
