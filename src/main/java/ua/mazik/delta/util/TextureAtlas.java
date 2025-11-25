package ua.mazik.delta.util;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import ua.mazik.delta.renderer.Renderer;
import ua.mazik.delta.renderer.Texture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextureAtlas implements AutoCloseable {
    public final int width;
    public final int height;

    private final List<Page> pages = new ArrayList<>();

    public TextureAtlas(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void add(@NonNull String name, @NonNull Pixmap pixmap) {
        Page page;

        if (this.pages.isEmpty()) {
            page = new Page(this.width, this.height);
            this.pages.add(page);
        } else {
            page = this.pages.get(this.pages.size() - 1);
        }

        if (!page.add(name, pixmap)) {
            this.pages.add(new Page(this.width, this.height));
            this.add(name, pixmap);
        }
    }

    public @Nullable Region findRegion(@NonNull String name) {
        for (Page page : this.pages) {
            for (Region region : page.regions) {
                if (region.name.equals(name)) {
                    return region;
                }
            }
        }

        return null;
    }

    public @NonNull List<Page> getPages() {
        return Collections.unmodifiableList(this.pages);
    }

    @Override
    public void close() {
        this.pages.forEach(Page::close);
    }

    public static class Page implements AutoCloseable {
        public final int width;
        public final int height;

        private final Pixmap pixmap;
        private final Texture texture;

        private final List<Region> regions = new ArrayList<>();

        private Page(int width, int height) {
            this.width = width;
            this.height = height;

            this.pixmap = new Pixmap(width, height);
            this.texture = this.pixmap.toTexture();
        }

        private boolean add(@NonNull String name, @NonNull Pixmap pixmap) {
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    boolean collision = false;

                    Region newRegion = new Region(x, y, pixmap.width, pixmap.height, name, this);

                    for (Region region : regions) {
                        if (this.overlaps(newRegion, region)) {
                            collision = true;
                            break;
                        }
                    }

                    if (collision) continue;

                    this.regions.add(newRegion);
                    this.pixmap.drawPixmap(x, y, pixmap);

                    Renderer.getInstance().subImage(this.texture, pixmap, x, y, pixmap.width, pixmap.height);

                    return true;
                }
            }

            return false;
        }

        private boolean overlaps(@NonNull Region a, @NonNull Region b) {
            return a.x < b.x + b.width &&
                    a.x + a.width > b.x &&
                    a.y < b.y + b.height &&
                    a.y + a.height > b.y;
        }

        public Texture getTexture() {
            return this.texture;
        }

        @Override
        public void close() {
            this.pixmap.close();
            this.texture.close();
        }
    }

    public record Region(int x, int y, int width, int height, @NonNull String name, @NonNull Page page) {

    }
}
