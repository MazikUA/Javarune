package eu.mazikkk.javarune.test;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import eu.mazikkk.javarune.codec.Codec;
import eu.mazikkk.javarune.codec.Codecs;
import eu.mazikkk.javarune.codec.RecordCodec;

import java.util.Optional;

public class CodecTests {
    public static void run() {
        Gson gson = new Gson();

        String json = """
            {
                "field1": 1,
                "field3": {
                    "field1": 1,
                    "field2": 2,
                    "field3": 3
                }
            }
            """;

        TestRecord2 result = TestRecord2.CODEC.decode(JsonParser.parseString(json));
        System.out.println(result);
        System.out.println(TestRecord2.CODEC.encode(result).toString());
    }

    public record TestRecord1(int field1, int field2, int field3) {
        public static final Codec<TestRecord1> CODEC = RecordCodec.create(
            Codecs.INT.fieldOf("field1").getter(TestRecord1::field1),
            Codecs.INT.fieldOf("field2").getter(TestRecord1::field2),
            Codecs.INT.fieldOf("field3").getter(TestRecord1::field3),
            TestRecord1::new
        ).asCodec();
    }

    public record TestRecord2(int field1, Optional<String> field2, TestRecord1 field3) {
        public static final Codec<TestRecord2> CODEC = RecordCodec.create(
            Codecs.INT.fieldOf("field1").getter(TestRecord2::field1),
            Codecs.STRING.optional().fieldOf("field2").getter(TestRecord2::field2),
            TestRecord1.CODEC.fieldOf("field3").getter(TestRecord2::field3),
            TestRecord2::new
        ).asCodec();
    }
}
