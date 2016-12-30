package net.catchpole.trebuchet.spoon;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.Filter;

public class MatchAllFilter<T extends CtElement> implements Filter<T> {
    @Override
    public boolean matches(T t) {
        return true;
    }
}
