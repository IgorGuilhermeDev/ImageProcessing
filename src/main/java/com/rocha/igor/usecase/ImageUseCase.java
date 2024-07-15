package com.rocha.igor.usecase;

import com.rocha.igor.domain.MatrixConsumer;
import com.rocha.igor.domain.Scale;

import java.util.function.Consumer;

public interface ImageUseCase {

    void dominantColor();

    <T> void grayScale(int [][] matR, int [][] matG, int [][] matB, MatrixConsumer<T, T, T> generateImage, Scale scale);

    void binaryImage();

    void negativeImage();

    void removeColor();

    <T> void rotate(int [][] matR, int [][] matG, int [][] matB, MatrixConsumer<T, T, T> generateImage, Double angle);
}
