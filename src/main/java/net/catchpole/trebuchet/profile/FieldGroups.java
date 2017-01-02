package net.catchpole.trebuchet.profile;

import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.*;
import spoon.reflect.visitor.Filter;

import java.util.ArrayList;
import java.util.List;

public class FieldGroups {
    private List<CtField> allFields = new ArrayList<CtField>();
    private List<CtField> staticFields = new ArrayList<CtField>();
    private List<CtField> staticInitializedFields = new ArrayList<CtField>();
    private List<CtField> memberFields = new ArrayList<CtField>();
    private List<CtField> memberInitializedFields = new ArrayList<CtField>();

    public FieldGroups(CtType ctType) {
        for (CtTypeMember ctTypeMember : (List<CtTypeMember>) ctType.getTypeMembers()) {
            if (ctTypeMember instanceof CtField) {
                CtField ctField = (CtField)ctTypeMember;
                allFields.add(ctField);
                if (isStatic(ctField)) {
                    staticFields.add(ctField);
                    if (!getFieldInitializer(ctField).isEmpty()) {
                        staticInitializedFields.add(ctField);
                    }
                } else {
                    memberFields.add(ctField);
                    if (!getFieldInitializer(ctField).isEmpty()) {
                        memberInitializedFields.add(ctField);
                    }
                }
            }
        }
    }

    public List<CtField> getAllFields() {
        return allFields;
    }

    public List<CtField> getStaticFields() {
        return staticFields;
    }

    public List<CtField> getStaticInitializedFields() {
        return staticInitializedFields;
    }

    public List<CtField> getMemberFields() {
        return memberFields;
    }

    public List<CtField> getMemberInitializedFields() {
        return memberInitializedFields;
    }

    public List<CtLiteral> getFieldInitializer(CtField ctField) {
        List<CtLiteral> ctLiterals = new ArrayList<CtLiteral>();
        for (CtElement ctElement : ctField.getElements(new Filter<CtLiteral>() {
            @Override
            public boolean matches(CtLiteral ctLiteral) {
                return true;
            }
        })) {
            ctLiterals.add((CtLiteral)ctElement);
        }
        return ctLiterals;
    }

    public boolean isAbstract(CtField ctField) {
        return ctField.getModifiers().contains(ModifierKind.ABSTRACT);
    }

    public boolean isStatic(CtField ctField) {
        return ctField.getModifiers().contains(ModifierKind.STATIC);
    }
}
