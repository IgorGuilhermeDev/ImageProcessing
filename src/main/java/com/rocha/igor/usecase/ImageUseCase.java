package com.rocha.igor.usecase;

import com.rocha.igor.domain.FilterType;
import com.rocha.igor.domain.MatrixConsumer;
import com.rocha.igor.domain.Scale;

public interface ImageUseCase {

    void dominantColor();

    <T> void grayScale(int [][] matR, int [][] matG, int [][] matB, MatrixConsumer<T, T, T> generateImage, Scale scale);
    int[][] grayScale(int [][] matR, int [][] matG, int [][] matB, Scale scale);

    void binaryImage();

    void negativeImage();

    void removeColor();

    <T> void rotate(int [][] matR, int [][] matG, int [][] matB, MatrixConsumer<T, T, T> generateImage, Double angle);
    <T> void filter(int [][] matR, int [][] matG, int [][] matB, MatrixConsumer<T, T, T> generateImage, int kernelSize, FilterType filterType, boolean useMedian);
}
