package net.catchpole.trebuchet.profile;

import spoon.reflect.code.BinaryOperatorKind;

import java.util.HashMap;
import java.util.Map;

public class BinaryOperatorMapping {
    private Map<BinaryOperatorKind,String> mapping = new HashMap<BinaryOperatorKind,String>();

    public BinaryOperatorMapping() {
        mapping.put(BinaryOperatorKind.AND,"&&");
        mapping.put(BinaryOperatorKind.OR,"||");
        mapping.put(BinaryOperatorKind.BITOR,"|");
        mapping.put(BinaryOperatorKind.BITXOR,"^");
        mapping.put(BinaryOperatorKind.BITAND,"&");
        mapping.put(BinaryOperatorKind.EQ,"==");
        mapping.put(BinaryOperatorKind.NE,"!=");
        mapping.put(BinaryOperatorKind.LT,"<");
        mapping.put(BinaryOperatorKind.GT,">");
        mapping.put(BinaryOperatorKind.LE,"<=");
        mapping.put(BinaryOperatorKind.GE,">=");
        mapping.put(BinaryOperatorKind.SL,"<<");
        mapping.put(BinaryOperatorKind.SR,">>");
        mapping.put(BinaryOperatorKind.USR,">>>");
        mapping.put(BinaryOperatorKind.PLUS,"+");
        mapping.put(BinaryOperatorKind.MINUS,"-");
        mapping.put(BinaryOperatorKind.MUL,"*");
        mapping.put(BinaryOperatorKind.DIV,"/");
        mapping.put(BinaryOperatorKind.MOD,"%");
        mapping.put(BinaryOperatorKind.INSTANCEOF,"¯\\_(ツ)_/¯");
    }

    public String getMapping(BinaryOperatorKind binaryOperatorKind) {
        return mapping.get(binaryOperatorKind);
    }
}
