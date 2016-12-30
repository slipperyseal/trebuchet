package net.catchpole.trebuchet.spoon;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.Filter;

public class MatchTypeFilter<T extends CtElement> implements Filter<T> {
    private Class type;

    public MatchTypeFilter(Class type) {
        this.type = type;
    }

    @Override
    public boolean matches(T t) {
        return type.isAssignableFrom(t.getClass());
    }
}