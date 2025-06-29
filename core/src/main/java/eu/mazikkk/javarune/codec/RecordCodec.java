package eu.mazikkk.javarune.codec;

import eu.mazikkk.javarune.util.function.PentaFunction;
import eu.mazikkk.javarune.util.function.QuadFunction;
import eu.mazikkk.javarune.util.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Function;

public record RecordCodec<T, I>(Function<I, T> getter, ObjectCodec<T> objectCodec) {
    public static <T1, R> ObjectCodec<R> create(RecordCodec<T1, R> c1, Function<T1, R> constructor) {
        return ObjectCodec.of(
            object -> constructor.apply(c1.objectCodec.decode(object)),
            (value, object) -> c1.objectCodec.encode(c1.getter.apply(value), object)
        );
    }

    public static <T1, T2, R> ObjectCodec<R> create(RecordCodec<T1, R> c1, RecordCodec<T2, R> c2, BiFunction<T1, T2, R> constructor) {
        return ObjectCodec.of(
            object -> constructor.apply(c1.objectCodec.decode(object), c2.objectCodec.decode(object)),
            (value, object) -> {
                c1.objectCodec.encode(c1.getter.apply(value), object);
                c2.objectCodec.encode(c2.getter.apply(value), object);
            }
        );
    }

    public static <T1, T2, T3, R> ObjectCodec<R> create(RecordCodec<T1, R> c1, RecordCodec<T2, R> c2, RecordCodec<T3, R> c3, TriFunction<T1, T2, T3, R> constructor) {
        return ObjectCodec.of(
            object -> constructor.apply(
                c1.objectCodec.decode(object),
                c2.objectCodec.decode(object),
                c3.objectCodec.decode(object)
            ),
            (value, object) -> {
                c1.objectCodec.encode(c1.getter.apply(value), object);
                c2.objectCodec.encode(c2.getter.apply(value), object);
                c3.objectCodec.encode(c3.getter.apply(value), object);
            }
        );
    }

    public static <T1, T2, T3, T4, R> ObjectCodec<R> create(RecordCodec<T1, R> c1, RecordCodec<T2, R> c2, RecordCodec<T3, R> c3, RecordCodec<T4, R> c4, QuadFunction<T1, T2, T3, T4, R> constructor) {
        return ObjectCodec.of(
            object -> constructor.apply(
                c1.objectCodec.decode(object),
                c2.objectCodec.decode(object),
                c3.objectCodec.decode(object),
                c4.objectCodec.decode(object)
            ),
            (value, object) -> {
                c1.objectCodec.encode(c1.getter.apply(value), object);
                c2.objectCodec.encode(c2.getter.apply(value), object);
                c3.objectCodec.encode(c3.getter.apply(value), object);
                c4.objectCodec.encode(c4.getter.apply(value), object);
            }
        );
    }

    public static <T1, T2, T3, T4, T5, R> ObjectCodec<R> create(RecordCodec<T1, R> c1, RecordCodec<T2, R> c2, RecordCodec<T3, R> c3, RecordCodec<T4, R> c4, RecordCodec<T5, R> c5, PentaFunction<T1, T2, T3, T4, T5, R> constructor) {
        return ObjectCodec.of(
            object -> constructor.apply(
                c1.objectCodec.decode(object),
                c2.objectCodec.decode(object),
                c3.objectCodec.decode(object),
                c4.objectCodec.decode(object),
                c5.objectCodec.decode(object)
            ),
            (value, object) -> {
                c1.objectCodec.encode(c1.getter.apply(value), object);
                c2.objectCodec.encode(c2.getter.apply(value), object);
                c3.objectCodec.encode(c3.getter.apply(value), object);
                c4.objectCodec.encode(c4.getter.apply(value), object);
                c5.objectCodec.encode(c5.getter.apply(value), object);
            }
        );
    }
}
