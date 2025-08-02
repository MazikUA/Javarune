package eu.mazikkk.javarune.test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.mazikkk.javarune.codec.Codec;
import eu.mazikkk.javarune.codec.Codecs;
import eu.mazikkk.javarune.codec.ObjectCodec;
import eu.mazikkk.javarune.codec.RecordCodec;
import eu.mazikkk.javarune.util.StringIdentifiable;

import java.util.ArrayList;
import java.util.List;

public class DispatchTests {
    public static void run() {
        String json = """
            {
                "spaces": [
                    {
                        "type": "values",
                        "values": [256, 320]
                    },
                    {
                        "type": "range",
                        "from": -256,
                        "to": 128,
                        "step": 2
                    }
                ]
            }
            """;

        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        ObjectCodec<List<Advance>> codec = Advance.CODEC.list().propertyOf("spaces");
        List<Advance> advances = codec.decode(obj);
        System.out.println(advances);
        JsonObject obj2 = new JsonObject();
        codec.encode(advances, obj2);
        System.out.println(obj2);
    }

    public enum AdvanceType implements StringIdentifiable {
        VALUES("values", ValuesAdvance.CODEC),
        RANGE("range", RangeAdvance.CODEC);

        public static final Codec<AdvanceType> CODEC = StringIdentifiable.createCodec(AdvanceType::values);

        private final String id;
        private final ObjectCodec<? extends Advance> codec;

        AdvanceType(String id, ObjectCodec<? extends Advance> codec) {
            this.id = id;
            this.codec = codec;
        }

        @Override
        public String asString() {
            return this.id;
        }

        public ObjectCodec<? extends Advance> getCodec() {
            return this.codec;
        }
    }

    public interface Advance {
        Codec<Advance> CODEC = AdvanceType.CODEC.dispatch("type", Advance::getType, AdvanceType::getCodec);

        List<Float> getValues();

        AdvanceType getType();
    }

    public record ValuesAdvance(List<Float> values) implements Advance {
        public static final ObjectCodec<ValuesAdvance> CODEC = Codecs.FLOAT.list().map(ValuesAdvance::new, ValuesAdvance::values).propertyOf("values");

        @Override
        public List<Float> getValues() {
            return this.values;
        }

        @Override
        public AdvanceType getType() {
            return AdvanceType.VALUES;
        }
    }

    public record RangeAdvance(float from, float to, float step) implements Advance {
        public static final ObjectCodec<RangeAdvance> CODEC = RecordCodec.create(
            Codecs.FLOAT.propertyOf("from").getter(RangeAdvance::from),
            Codecs.FLOAT.propertyOf("to").getter(RangeAdvance::to),
            Codecs.FLOAT.optional(1.0f).propertyOf("step").getter(RangeAdvance::step),
            RangeAdvance::new
        );

        @Override
        public List<Float> getValues() {
            List<Float> values = new ArrayList<>();

            for (float i = from; i <= to; i += step) {
                values.add(i);
            }

            return values;
        }

        @Override
        public AdvanceType getType() {
            return AdvanceType.RANGE;
        }
    }
}
