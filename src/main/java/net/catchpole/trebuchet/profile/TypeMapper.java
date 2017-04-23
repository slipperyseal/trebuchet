package net.catchpole.trebuchet.profile;

import spoon.reflect.declaration.CtTypeInformation;

import java.util.HashMap;
import java.util.Map;

public class TypeMapper {
    private Map<String,String> typeMappings = new HashMap<String, String>();

    public TypeMapper() {
        typeMappings.put("java.lang.String", "const char");
        typeMappings.put("java.lang.reflect.Array", "char");
        typeMappings.put("boolean", "bool");
        typeMappings.put("long", "long long");
        typeMappings.put("char", "short");
        typeMappings.put("byte", "char");
    }

    public String getTypeName(CtTypeInformation ctTypeInformation) {
        String javaName = ctTypeInformation.getQualifiedName();
        String mappedName = typeMappings.get(javaName);
        if (mappedName == null) {
            mappedName = mapClassNameShortened(javaName);
            if (typeMappings.containsValue(mappedName)) {
                // if short mapped name already exists dropback to explicit package_Class__InnerClass format
                mappedName = mapClassNameVerbose(javaName);
            }
            typeMappings.put(javaName, mappedName);
        }
        return mappedName;
    }

    public String getTypeReference(CtTypeInformation ctTypeInformation) {
        String name = getTypeName(ctTypeInformation);
        if (!ctTypeInformation.isPrimitive()) {
            return name + " *";
        }
        return name;
    }

    private String mapClassNameShortened(String className) {
        int innerClass = className.lastIndexOf('$');
        if (innerClass != -1) {
            className = className.substring(innerClass+1);
        }
        int dot = className.lastIndexOf('.');
        if (dot != -1) {
            className = className.substring(dot+1);
        }
        return className;
    }

    private String mapClassNameVerbose(String className) {
        return className.replace(".", "_").replace("$", "__");
    }
}
