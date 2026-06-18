package ua.mazik.delta.util;

import java.util.function.Supplier;

public class Lazy<T> {
    private final Supplier<T> supplier;

    private T value;

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (this.value == null) {
            this.value = this.supplier.get();
        }
        return this.value;
    }
}
