package ua.mazik.delta.renderer.draw;

import ua.mazik.delta.renderer.VertexBuilder;
import ua.mazik.delta.renderer.vertex.VertexFormat;
import ua.mazik.delta.util.Bindable;

public interface DrawElement<T> extends Bindable {
    VertexFormat<T> format();

    void build(VertexBuilder<T> builder);

    boolean isDirty(DrawElement<T> previous);
}
