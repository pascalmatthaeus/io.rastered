package io.rastered.app.core;

@FunctionalInterface
public interface FilterFunction<I,O> {
    public O apply( Texture target, I... args );
}