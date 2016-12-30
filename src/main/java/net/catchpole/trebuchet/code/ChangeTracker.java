package net.catchpole.trebuchet.code;

public class ChangeTracker<T> {
    private final T defaultValue;
    private T value;

    public ChangeTracker(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public T changedValue(T value) {
        if (value == null) {
            value = this.defaultValue;
        }
        if (this.value == null || !this.value.equals(value)) {
            return this.value = value;
        } else {
            this.value = value;
        }
        return null;
    }
}
