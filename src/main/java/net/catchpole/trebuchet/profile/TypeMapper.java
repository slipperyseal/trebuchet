package net.catchpole.trebuchet.profile;

import spoon.reflect.declaration.CtTypeInformation;

import java.util.HashMap;
import java.util.Map;

public class TypeMapper {
    private Map<String,String> typeMappings = new HashMap<String, String>();

    public TypeMapper() {
        typeMappings.put("java.lang.String", "char *");
        typeMappings.put("java.lang.reflect.Array", "char **");
        typeMappings.put("boolean", "bool");
        typeMappings.put("long", "long long");
    }

    public String getTypeName(CtTypeInformation ctTypeInformation) {
        String javaName = ctTypeInformation.getQualifiedName();
        String mappedName = typeMappings.get(javaName);
        if (mappedName == null) {
            mappedName = mapClassName(javaName);
            if (typeMappings.containsValue(mappedName)) {
                // if short mapped name already exists dropback to explicit package_Class format
                mappedName = javaName.replace(".","_");
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

    private String mapClassName(String name) {
        int dot = name.lastIndexOf('.');
        if (dot != -1) {
            name = name.substring(dot+1);
        }
        return name;
    }
}
