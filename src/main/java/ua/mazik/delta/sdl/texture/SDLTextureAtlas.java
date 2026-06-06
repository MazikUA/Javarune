package ua.mazik.delta.sdl.texture;

import ua.mazik.delta.sdl.renderer.SDLRenderer;
import ua.mazik.delta.util.PixelData;

import java.util.*;

public class SDLTextureAtlas implements AutoCloseable {
    public final int width;
    public final int height;

    public final SDLRenderer renderer;

    private final List<Page> pages;

    public SDLTextureAtlas(int width, int height, SDLRenderer renderer) {
        this.width = width;
        this.height = height;

        this.renderer = renderer;

        this.pages = new ArrayList<>();
    }

    public void add(String name, PixelData pixels) {
        if (pixels.width() > this.width || pixels.height() > this.height) {
            throw new IllegalArgumentException();
        }

        for (Page page : this.pages) {
            if (page.regions.containsKey(name)) {
                throw new IllegalArgumentException();
            }
        }

        Page page;

        if (this.pages.isEmpty()) {
            page = new Page(this);
            this.pages.add(page);
        } else {
            page = this.pages.get(this.pages.size() - 1);
        }

        if (!page.add(name, pixels)) {
            this.pages.add(new Page(this));
            this.add(name, pixels);
        }
    }

    public Optional<Region> findRegion(String name) {
        for (Page page : this.pages) {
            Region region = page.regions.get(name);

            if (region != null) {
                return Optional.of(region);
            }
        }
        return Optional.empty();
    }

    @Override
    public void close() {
        this.pages.forEach(Page::close);
        this.pages.clear();
    }

    public static final class Page implements AutoCloseable {
        private final SDLTextureAtlas atlas;
        private final SDLTexture texture;

        private final Map<String, Region> regions;

        private Page(SDLTextureAtlas atlas) {
            this.atlas = atlas;
            this.texture = new SDLTexture(atlas.width, atlas.height, SDLTexture.Access.STREAMING, atlas.renderer);

            this.regions = new HashMap<>();
        }

        public boolean add(String name, PixelData pixels) {
            for (int x = 0; x <= this.atlas.width - pixels.width(); x++) {
                for (int y = 0; y <= this.atlas.height - pixels.height(); y++) {
                    Region newRegion = new Region(x, y, pixels.width(), pixels.height(), this);

                    boolean collision = false;

                    for (Map.Entry<String, Region> r : this.regions.entrySet()) {
                        if (this.overlaps(newRegion, r.getValue())) {
                            collision = true;
                            break;
                        }
                    }

                    if (collision) continue;

                    this.texture.update(x, y, pixels.width(), pixels.height(), pixels);
                    this.regions.put(name, newRegion);

                    return true;
                }
            }
            return false;
        }

        private boolean overlaps(Region a, Region b) {
            return a.x < b.x + b.w &&
                a.x + a.w > b.x &&
                a.y < b.y + b.h &&
                a.y + a.h > b.y;
        }

        @Override
        public void close() {
            this.regions.clear();
            this.texture.close();
        }
    }

    public record Region(int x, int y, int w, int h, Page page) {
        public void draw(int x, int y) {
            page.texture.draw(x, y, this.w, this.h, this.x, this.y, this.w, this.h);
        }
    }
}
