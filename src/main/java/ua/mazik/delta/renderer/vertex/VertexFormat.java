package ua.mazik.delta.renderer.vertex;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class VertexFormat<T> {
    public final List<Attribute<T, ?>> attributes;
    public final boolean blend;

    public final int stride;

    public VertexFormat(List<Attribute<T, ?>> attributes, boolean blend) {
        this.attributes = attributes;
        this.blend = blend;

        this.stride = attributes.stream()
                .mapToInt(attribute -> attribute.type.size() * attribute.type.byteSize())
                .sum();
    }

    public void put(ByteBuffer buffer, T obj) {
        for (Attribute<T, ?> attribute : this.attributes) {
            attribute.put(buffer, obj);
        }
    }

    public record Attribute<T, R>(String name, VertexType<R> type, boolean normalized, Function<T, R> getter) {
        public void put(ByteBuffer buffer, T obj) {
            type.consumer().accept(getter.apply(obj), buffer);
        }
    }

    public static class Builder<T> {
        private final List<Attribute<T, ?>> attributes = new ArrayList<>();
        private boolean blend;

        public <R> Builder<T> attribute(String name, VertexType<R> type, boolean normalized, Function<T, R> getter) {
            this.attributes.add(new Attribute<>(name, type, normalized, getter));
            return this;
        }

        public <R> Builder<T> attribute(String name, VertexType<R> type, Function<T, R> getter) {
            return this.attribute(name, type, false, getter);
        }

        public Builder<T> blend(boolean blend) {
            this.blend = blend;
            return this;
        }

        public VertexFormat<T> build() {
            return new VertexFormat<>(this.attributes, this.blend);
        }
    }
}
