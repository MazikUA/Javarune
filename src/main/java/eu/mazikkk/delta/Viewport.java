package eu.mazikkk.delta;

public abstract class Viewport {
    public final Scaling scaling;
    public final Camera camera;

    private int x;
    private int y;
    private int width;
    private int height;

    public Viewport(int width, int height, Scaling scaling, Camera camera) {
        this.setSize(width, height);

        this.scaling = scaling;
        this.camera = camera;
    }

    public abstract void enableScissors();

    public abstract void disableScissors();

    public void apply() {
        this.camera.apply();
    }

    public void resize(int windowWidth, int windowHeight) {
        this.scaling.resize(windowWidth, windowHeight, this);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @FunctionalInterface
    public interface Scaling {
        Scaling FIT = (windowWidth, windowHeight, viewport) -> {
            if (windowWidth <= 0 || windowHeight <= 0) return;

            float scale = Math.min((float) windowWidth / viewport.camera.getWorldWidth(), (float) windowHeight / viewport.camera.getWorldHeight());

            int scaledWidth = Math.round(viewport.camera.getWorldWidth() * scale);
            int scaledHeight = Math.round(viewport.camera.getWorldHeight() * scale);

            int scaledX = (windowWidth - scaledWidth) / 2;
            int scaledY = (windowHeight - scaledHeight) / 2;

            viewport.setSize(scaledWidth, scaledHeight);
            viewport.setPosition(scaledX, scaledY);
        };

        void resize(int windowWidth, int windowHeight, Viewport viewport);
    }
}
