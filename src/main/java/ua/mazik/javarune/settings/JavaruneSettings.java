package ua.mazik.javarune.settings;

import ua.mazik.delta.codec.Codecs;
import ua.mazik.delta.fs.DeltaFile;
import ua.mazik.delta.settings.DeltaSettings;

public class JavaruneSettings extends DeltaSettings {
    public final DeltaSettings.Field<String> language = this.addField("language", Codecs.STRING, "en_us");

    public JavaruneSettings(DeltaFile file) {
        super(file);
    }
}
