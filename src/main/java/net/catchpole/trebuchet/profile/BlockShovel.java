package net.catchpole.trebuchet.profile;

import net.catchpole.trebuchet.code.CodeWriter;
import net.catchpole.trebuchet.spoon.MatchAllFilter;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;

public class BlockShovel {
    private CodeWriter codeWriter;
    private TypeMapper typeMapper;

    public BlockShovel(CodeWriter codeWriter, TypeMapper typeMapper) {
        this.codeWriter = codeWriter;
        this.typeMapper = typeMapper;
    }

    public void addBlock(CtBlock ctBlock) {
        if (ctBlock == null) {
            return;
        }

        System.out.println("BLOCK: " + ctBlock.getParent().toString());
        for (CtStatement ctStatement : ctBlock.getStatements()) {
            System.out.println("    STATEMENT: " + ctStatement);

            StatementShovel statementShovel = new StatementShovel(codeWriter, typeMapper);
            if (ctStatement.toString().equals("super()")) {
                // skip super() calls for now
                continue;
            }
            for (CtElement ctElement : ctStatement.getElements(new MatchAllFilter<CtElement>())) {
                System.out.println("        ELEMENT: " + ctElement.getClass().getSimpleName() + " " + ctElement);
                statementShovel.addElement(ctElement);
            }

            statementShovel.finish();
        }
    }
}
