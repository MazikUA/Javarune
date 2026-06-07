package ua.mazik.delta.settings;

import com.google.gson.*;
import ua.mazik.delta.codec.Codec;
import ua.mazik.delta.fs.DeltaFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The {@link DeltaSettings} class allows to simply creating {@code .json} files with
 * different fields.
 */
public class DeltaSettings {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final DeltaFile file;
    private final Map<String, Field<?>> fields;

    public DeltaSettings(DeltaFile file) {
        this.file = file;
        this.fields = new HashMap<>();
    }

    /**
     * Adds new field to {@link DeltaSettings} instance. The field would
     * be serialized on {@link DeltaSettings#read} and {@link DeltaSettings#write}.
     *
     * @param name  Field name.
     * @param codec Codec used for field type.
     * @param value Default value.
     * @param <T>   Field type.
     * @return {@link Field} instance.
     */
    public <T> Field<T> addField(String name, Codec<T> codec, T value) {
        Field<T> field = new Field<>(codec, value);

        this.fields.put(name, field);

        return field;
    }

    /**
     * Adds new field to {@link DeltaSettings} instance. The field would
     * be serialized on {@link DeltaSettings#read} and {@link DeltaSettings#write}.
     * Default value is set to {@code null}.
     *
     * @param name  Field name.
     * @param codec Codec used for field type.
     * @param <T>   Field type.
     * @return {@link Field} instance.
     */
    public <T> Field<T> addField(String name, Codec<T> codec) {
        return this.addField(name, codec, null);
    }

    /**
     * Reads file content and updates {@link Field}'s values.
     */
    public void read() {
        Optional<String> content = this.file.readAsString();

        if (content.isEmpty()) return;

        JsonElement element;

        try {
            element = JsonParser.parseString(content.get());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return;
        }

        if (!element.isJsonObject()) return;

        for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
            Field<?> field = this.fields.get(entry.getKey());

            if (field != null) {
                field.set(entry.getValue());
            }
        }
    }

    /**
     * Writes {@link Field}s to {@code .json} file.
     */
    public void write() {
        JsonObject obj = new JsonObject();

        for (Map.Entry<String, Field<?>> entry : this.fields.entrySet()) {
            entry.getValue().encode().ifPresent(json -> {
                obj.add(entry.getKey(), json);
            });
        }

        this.file.write(GSON.toJson(obj));
    }

    /**
     * The {@link Field} class allows to simply set fields inside {@link DeltaSettings}.
     *
     * @param <T> Field type.
     */
    public static final class Field<T> {
        public final Codec<T> codec;
        public final T initialValue;

        private T value;

        private Field(Codec<T> codec, T value) {
            this.codec = codec;
            this.value = value;
            this.initialValue = value;
        }

        /**
         * Sets {@link Field} value to decoded {@link JsonElement} using {@link Field#codec}.
         *
         * @param element {@link JsonElement} which will be serialized.
         */
        public void set(JsonElement element) {
            T decoded = this.codec.decode(element);

            this.value = decoded != null ? decoded : this.initialValue;
        }

        /**
         * Sets {@link Field} value.
         *
         * @param value New field value.
         */
        public void set(T value) {
            this.value = value != null ? value : this.initialValue;
        }

        /**
         * @return {@link Field} value wrapped in {@link Optional}. Can be empty.
         */
        public Optional<T> getSafe() {
            return Optional.ofNullable(this.value);
        }

        /**
         * @return {@link Field} value. Can be null.
         */
        public T get() {
            return this.value;
        }

        /**
         * @return Encoded {@link JsonElement} wrapped in {@link Optional}. Can be empty.
         */
        public Optional<JsonElement> encode() {
            return this.getSafe().map(this.codec::encode);
        }
    }
}
