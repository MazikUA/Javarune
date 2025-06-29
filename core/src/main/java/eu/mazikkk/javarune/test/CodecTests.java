package eu.mazikkk.javarune.test;

import com.google.gson.JsonParser;
import eu.mazikkk.javarune.codec.Codec;
import eu.mazikkk.javarune.codec.Codecs;
import eu.mazikkk.javarune.codec.RecordCodec;

import java.util.Optional;

public class CodecTests {
    public static void run() {
        String json = """
            {
                "property1": 1,
                "property3": {
                    "property1": 1,
                    "property2": 2,
                    "property3": 3
                }
            }
            """;

        TestRecord2 result = TestRecord2.CODEC.decode(JsonParser.parseString(json));
        System.out.println(result);
        System.out.println(TestRecord2.CODEC.encode(result).toString());
    }

    public record TestRecord1(int property1, int property2, int property3) {
        public static final Codec<TestRecord1> CODEC = RecordCodec.create(
            Codecs.INT.propertyOf("property1").getter(TestRecord1::property1),
            Codecs.INT.propertyOf("property2").getter(TestRecord1::property2),
            Codecs.INT.propertyOf("property3").getter(TestRecord1::property3),
            TestRecord1::new
        ).toCodec();
    }

    public record TestRecord2(int property1, Optional<String> property2, TestRecord1 property3) {
        public static final Codec<TestRecord2> CODEC = RecordCodec.create(
            Codecs.INT.propertyOf("property1").getter(TestRecord2::property1),
            Codecs.STRING.optional().propertyOf("property2").getter(TestRecord2::property2),
            TestRecord1.CODEC.propertyOf("property3").getter(TestRecord2::property3),
            TestRecord2::new
        ).toCodec();
    }
}
