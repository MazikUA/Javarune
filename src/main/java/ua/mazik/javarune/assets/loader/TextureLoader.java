package ua.mazik.javarune.assets.loader;

import ua.mazik.delta.assets.Asset;
import ua.mazik.delta.assets.AssetLoader;
import ua.mazik.delta.assets.AssetSource;
import ua.mazik.delta.sdl.renderer.SDLRenderer;
import ua.mazik.delta.sdl.texture.SDLBitmap;
import ua.mazik.delta.sdl.texture.SDLTexture;
import ua.mazik.delta.spng.SPNGImage;
import ua.mazik.delta.util.LWJGLUtil;

import java.util.Optional;

public class TextureLoader extends AssetLoader<SDLTexture> {
    public final SDLRenderer renderer;

    public TextureLoader(AssetSource assetSource, SDLRenderer renderer) {
        super(assetSource);

        this.renderer = renderer;
    }

    @Override
    public Optional<SDLTexture> load(String path) {
        Optional<Asset> asset = this.assetSource.getAsset("textures/" + path + ".png");

        if (asset.isPresent()) {
            byte[] bytes = asset.get().getBytes();

            SPNGImage png = SPNGImage.fromBuffer(LWJGLUtil.createBuffer(bytes));

            SDLBitmap bitmap = new SDLBitmap(png);
            SDLTexture texture = new SDLTexture(bitmap, this.renderer);

            bitmap.close();
            png.close();

            return Optional.of(texture);
        }

        return this.load("misc/assgore");
    }
}
