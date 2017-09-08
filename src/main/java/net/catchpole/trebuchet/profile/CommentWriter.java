package net.catchpole.trebuchet.profile;

import net.catchpole.trebuchet.code.CodeWriter;
import spoon.reflect.code.CtComment;

public class CommentWriter {
    private CodeWriter codeWriter;

    public CommentWriter(CodeWriter codeWriter) {
        this.codeWriter = codeWriter;
    }

    public void write(CtComment ctComment) {
        CtComment.CommentType commentType = ctComment.getCommentType();
        if (commentType.equals(CtComment.CommentType.FILE) ||
            ctComment.getCommentType().equals(CtComment.CommentType.BLOCK)) {
            codeWriter.println("/*");
        } else {
            codeWriter.print("// ");
        }
        codeWriter.println(ctComment.getContent());
        if (commentType.equals(CtComment.CommentType.FILE) ||
                ctComment.getCommentType().equals(CtComment.CommentType.BLOCK)) {
            codeWriter.println("*/");
        }
    }
}
