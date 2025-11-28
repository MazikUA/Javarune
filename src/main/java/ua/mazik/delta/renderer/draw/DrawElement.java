package ua.mazik.delta.renderer.draw;

import ua.mazik.delta.renderer.VertexBuilder;
import ua.mazik.delta.util.Bindable;

public interface DrawElement extends Bindable {
    void build(VertexBuilder builder);

    boolean isDirty(DrawElement previous);
}
