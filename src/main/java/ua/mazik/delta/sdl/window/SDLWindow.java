package ua.mazik.delta.sdl.window;

import org.lwjgl.sdl.*;
import ua.mazik.delta.sdl.util.SDLUtil;
import ua.mazik.delta.util.function.TriFunction;

import static org.lwjgl.sdl.SDLProperties.*;
import static org.lwjgl.sdl.SDLVideo.*;

public final class SDLWindow implements AutoCloseable {
    public final long address;

    public boolean fullscreen = false;

    private SDLWindow(long address) {
        this.address = address;
    }

    /**
     * Creates new {@link SDLWindow.Builder}.
     *
     * @return {@link SDLWindow.Builder} instance.
     */
    public static Builder builder() {
        return new SDLWindow.Builder();
    }

    public void show() {
        SDLUtil.check(SDL_ShowWindow(this.address));
    }

    public void hide() {
        SDLUtil.check(SDL_HideWindow(this.address));
    }

    public void toggleFullscreen() {
        this.fullscreen = !this.fullscreen;
        SDL_SetWindowFullscreen(this.address, this.fullscreen);
    }

    @Override
    public void close() {
        SDL_DestroyWindow(this.address);
    }

    public static final class Builder {
        private final WindowProp<String> title = new WindowProp<>("Window title", SDL_PROP_WINDOW_CREATE_TITLE_STRING, SDLProperties::SDL_SetStringProperty);
        private final WindowProp<Integer> x = new WindowProp<>(SDL_WINDOWPOS_UNDEFINED, SDL_PROP_WINDOW_CREATE_X_NUMBER, SDLProperties::SDL_SetNumberProperty);
        private final WindowProp<Integer> y = new WindowProp<>(SDL_WINDOWPOS_UNDEFINED, SDL_PROP_WINDOW_CREATE_Y_NUMBER, SDLProperties::SDL_SetNumberProperty);
        private final WindowProp<Integer> width = new WindowProp<>(640, SDL_PROP_WINDOW_CREATE_WIDTH_NUMBER, SDLProperties::SDL_SetNumberProperty);
        private final WindowProp<Integer> height = new WindowProp<>(480, SDL_PROP_WINDOW_CREATE_HEIGHT_NUMBER, SDLProperties::SDL_SetNumberProperty);
        private final WindowProp<Boolean> hidden = new WindowProp<>(false, SDL_PROP_WINDOW_CREATE_HIDDEN_BOOLEAN, SDLProperties::SDL_SetBooleanProperty);
        private final WindowProp<Boolean> resizable = new WindowProp<>(true, SDL_PROP_WINDOW_CREATE_RESIZABLE_BOOLEAN, SDLProperties::SDL_SetBooleanProperty);
        private int minWidth;
        private int minHeight;

        private Builder() {
        }

        /**
         * Sets window title.
         *
         * @param value Window title.
         * @return This {@link Builder}.
         */
        public Builder title(String value) {
            this.title.set(value);
            return this;
        }

        /**
         * Sets window X.
         *
         * @param value Horizontal position of window.
         * @return This {@link Builder}.
         */
        public Builder x(int value) {
            this.x.set(value);
            return this;
        }

        /**
         * Makes window horizontally centered.
         *
         * @return This {@link Builder}.
         */
        public Builder centeredX() {
            return this.x(SDL_WINDOWPOS_CENTERED);
        }

        /**
         * Sets window Y.
         *
         * @param value Vertical position of window.
         * @return This {@link Builder}.
         */
        public Builder y(int value) {
            this.y.set(value);
            return this;
        }

        /**
         * Makes window vertically centered.
         *
         * @return This {@link Builder}.
         */
        public Builder centeredY() {
            return this.y(SDL_WINDOWPOS_CENTERED);
        }

        /**
         * Makes window centered.
         *
         * @return This {@link Builder}.
         */
        public Builder centered() {
            return this.centeredX().centeredY();
        }

        /**
         * Sets window width.
         *
         * @param value Window width.
         * @return This {@link Builder}.
         */
        public Builder w(int value) {
            this.width.set(value);
            return this;
        }

        /**
         * Sets window height.
         *
         * @param value Window height.
         * @return This {@link Builder}.
         */
        public Builder h(int value) {
            this.height.set(value);
            return this;
        }

        /**
         * Sets size of window.
         *
         * @param w Window width.
         * @param h Window height.
         * @return This {@link Builder}.
         */
        public Builder size(int w, int h) {
            return this.w(w).h(h);
        }

        /**
         * Makes window hidden or shown.
         *
         * @param value Is window should be hidden.
         * @return This {@link Builder}.
         */
        public Builder hidden(boolean value) {
            this.hidden.set(value);
            return this;
        }

        /**
         * Makes window hidden.
         *
         * @return This {@link Builder}.
         */
        public Builder hidden() {
            return this.hidden(true);
        }

        /**
         * Makes window resizable or fixed size.
         *
         * @param value Is window should be resizable.
         * @return This {@link Builder}.
         */
        public Builder resizable(boolean value) {
            this.resizable.set(value);
            return this;
        }

        /**
         * Makes window resizable.
         *
         * @return This {@link Builder}.
         */
        public Builder resizable() {
            return this.resizable(true);
        }

        /**
         * Sets minimum size of window.
         *
         * @param w Minimum width.
         * @param h Minimum height.
         * @return This {@link Builder}.
         */
        public Builder minSize(int w, int h) {
            this.minWidth = w;
            this.minHeight = h;
            return this;
        }

        /**
         * Builds {@link SDLWindow} with given values.
         *
         * @return {@link SDLWindow} instance.
         */
        public SDLWindow build() {
            int props = SDL_CreateProperties();

            this.title.apply(props);

            this.x.apply(props);
            this.y.apply(props);

            this.width.apply(props);
            this.height.apply(props);

            this.hidden.apply(props);
            this.resizable.apply(props);

            long windowAddress = SDLUtil.check(SDL_CreateWindowWithProperties(props));

            SDL_DestroyProperties(props);

            if (this.minWidth > 0 && this.minHeight > 0) {
                SDLUtil.check(SDL_SetWindowMinimumSize(windowAddress, this.minWidth, this.minHeight));
            }

            return new SDLWindow(windowAddress);
        }
    }

    private static final class WindowProp<T> {
        private final CharSequence propertyName;
        private final TriFunction<Integer, CharSequence, T, Boolean> setter;
        private T value;

        private WindowProp(T value, CharSequence propertyName, TriFunction<Integer, CharSequence, T, Boolean> setter) {
            this.value = value;
            this.propertyName = propertyName;
            this.setter = setter;
        }

        public void apply(int props) {
            SDLUtil.check(setter.apply(props, this.propertyName, this.value));
        }

        public void set(T value) {
            this.value = value;
        }
    }
}
