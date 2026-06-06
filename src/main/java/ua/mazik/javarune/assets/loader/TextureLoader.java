package ua.mazik.javarune.assets.loader;

import ua.mazik.delta.assets.AssetLoader;
import ua.mazik.delta.assets.AssetSource;
import ua.mazik.delta.sdl.renderer.SDLRenderer;
import ua.mazik.delta.sdl.texture.SDLTexture;
import ua.mazik.javarune.Javarune;

import java.util.Optional;

public class TextureLoader extends AssetLoader<SDLTexture> {
    public final SDLRenderer renderer;

    public TextureLoader(AssetSource assetSource, SDLRenderer renderer) {
        super(assetSource);

        this.renderer = renderer;
    }

    @Override
    public Optional<SDLTexture> load(String path) {
        return Javarune.imageLoader().load(path).map(img -> new SDLTexture(img, this.renderer));
    }
}
