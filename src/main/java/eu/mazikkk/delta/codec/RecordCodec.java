package eu.mazikkk.delta.codec;

import eu.mazikkk.javarune.util.function.PentaFunction;
import eu.mazikkk.javarune.util.function.QuadFunction;
import eu.mazikkk.javarune.util.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Function;

public record RecordCodec<T, I>(Function<I, T> getter, ObjectCodec<T> objectCodec) {
    public static <T1, R> ObjectCodec<R> create(RecordCodec<T1, R> t1, Function<T1, R> constructor) {
        return ObjectCodec.of(
                object -> constructor.apply(t1.objectCodec.decode(object)),
                (value, object) -> t1.objectCodec.encode(t1.getter.apply(value), object)
        );
    }

    public static <T1, T2, R> ObjectCodec<R> create(RecordCodec<T1, R> t1, RecordCodec<T2, R> t2, BiFunction<T1, T2, R> constructor) {
        return ObjectCodec.of(
                object -> constructor.apply(t1.objectCodec.decode(object), t2.objectCodec.decode(object)),
                (value, object) -> {
                    t1.objectCodec.encode(t1.getter.apply(value), object);
                    t2.objectCodec.encode(t2.getter.apply(value), object);
                }
        );
    }

    public static <T1, T2, T3, R> ObjectCodec<R> create(RecordCodec<T1, R> t1, RecordCodec<T2, R> t2, RecordCodec<T3, R> t3, TriFunction<T1, T2, T3, R> constructor) {
        return ObjectCodec.of(
                object -> constructor.apply(
                        t1.objectCodec.decode(object),
                        t2.objectCodec.decode(object),
                        t3.objectCodec.decode(object)
                ),
                (value, object) -> {
                    t1.objectCodec.encode(t1.getter.apply(value), object);
                    t2.objectCodec.encode(t2.getter.apply(value), object);
                    t3.objectCodec.encode(t3.getter.apply(value), object);
                }
        );
    }

    public static <T1, T2, T3, T4, R> ObjectCodec<R> create(RecordCodec<T1, R> t1, RecordCodec<T2, R> t2, RecordCodec<T3, R> t3, RecordCodec<T4, R> t4, QuadFunction<T1, T2, T3, T4, R> constructor) {
        return ObjectCodec.of(
                object -> constructor.apply(
                        t1.objectCodec.decode(object),
                        t2.objectCodec.decode(object),
                        t3.objectCodec.decode(object),
                        t4.objectCodec.decode(object)
                ),
                (value, object) -> {
                    t1.objectCodec.encode(t1.getter.apply(value), object);
                    t2.objectCodec.encode(t2.getter.apply(value), object);
                    t3.objectCodec.encode(t3.getter.apply(value), object);
                    t4.objectCodec.encode(t4.getter.apply(value), object);
                }
        );
    }

    public static <T1, T2, T3, T4, T5, R> ObjectCodec<R> create(RecordCodec<T1, R> t1, RecordCodec<T2, R> t2, RecordCodec<T3, R> t3, RecordCodec<T4, R> t4, RecordCodec<T5, R> t5, PentaFunction<T1, T2, T3, T4, T5, R> constructor) {
        return ObjectCodec.of(
                object -> constructor.apply(
                        t1.objectCodec.decode(object),
                        t2.objectCodec.decode(object),
                        t3.objectCodec.decode(object),
                        t4.objectCodec.decode(object),
                        t5.objectCodec.decode(object)
                ),
                (value, object) -> {
                    t1.objectCodec.encode(t1.getter.apply(value), object);
                    t2.objectCodec.encode(t2.getter.apply(value), object);
                    t3.objectCodec.encode(t3.getter.apply(value), object);
                    t4.objectCodec.encode(t4.getter.apply(value), object);
                    t5.objectCodec.encode(t5.getter.apply(value), object);
                }
        );
    }
}
