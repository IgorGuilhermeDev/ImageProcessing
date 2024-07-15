package com.rocha.igor.domain;

@FunctionalInterface
public interface MatrixConsumer<R, G, B> {
    void accept(R matR, G matG, B matB );
}
