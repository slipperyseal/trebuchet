package net.catchpole.trebuchet.profile;

import spoon.reflect.declaration.CtTypeInformation;

import java.util.HashMap;
import java.util.Map;

public class TypeMapper {
    private Map<String,String> typeMappings = new HashMap<String, String>();

    public TypeMapper() {
        typeMappings.put("java.lang.String", "char *");
        typeMappings.put("java.lang.reflect.Array", "char **");
    }

    public String getTypeName(CtTypeInformation ctTypeInformation) {
        String javaName = ctTypeInformation.getQualifiedName();
        String mappedName = typeMappings.get(javaName);
        if (mappedName == null) {
            mappedName = mapName(javaName);
            typeMappings.put(javaName, mappedName);
        }
        return mappedName;
    }

    public String getTypeReference(CtTypeInformation ctTypeInformation) {
        String name = getTypeName(ctTypeInformation);
        if (name.charAt(0) >= 'A' && name.charAt(0) <= 'Z') {
            return name + " *";
        }
        return name;
    }

    private String mapName(String name) {
        if (name.startsWith("java.")) {
            return name.replace('.','_');
        }

        int dot = name.lastIndexOf('.');
        if (dot != -1) {
            name = name.substring(dot+1);
        }
        return name;
    }
}
