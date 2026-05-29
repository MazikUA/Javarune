package ua.mazik.delta.codec;

import java.util.function.Function;

public record GetterCodec<T, I>(Function<I, T> getter, ObjectCodec<T> objectCodec) {
}
