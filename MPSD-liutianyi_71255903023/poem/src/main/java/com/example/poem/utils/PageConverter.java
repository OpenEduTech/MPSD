package com.example.poem.utils;


import java.util.function.Function;

public class PageConverter<E, R> implements Function<E, R> {
    ToResponseConverter<E, R> converter;

    public PageConverter(ToResponseConverter<E, R> converter) {
        this.converter = converter;
    }

    @Override
    public R apply(E e) {
        return this.converter.toResponse(e);
    }
}
