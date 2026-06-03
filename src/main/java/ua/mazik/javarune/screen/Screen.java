package ua.mazik.javarune.screen;

import ua.mazik.javarune.render.RenderContext;

public abstract class Screen {
    public abstract void update();

    public abstract void render(RenderContext ctx);
}
