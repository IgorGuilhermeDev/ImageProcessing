package com.rocha.igor.usecase;

import com.rocha.igor.domain.MatrixConsumer;
import com.rocha.igor.domain.Scale;

import java.util.function.Consumer;

public class ImageUseCaseImpl implements ImageUseCase{
    @Override
    public <T> void grayScale(int[][] matR, int[][] matG, int[][] matB, MatrixConsumer<T, T, T> generateImage, Scale scale) {
        int [][] med = new int[matR.length][matR[0].length];
        for (int i = 0; i < matR.length; i++) {
            for (int j = 0; j < matR[0].length ; j++) {
                if(scale == Scale.NORMAL) med[i][j] = (matR[i][j] + matG[i][j] + matB[i][j]) / 3;
                else if (scale == Scale.LIGHT) med[i][j] = Math.max(matR[i][j], Math.max(matG[i][j], matB[i][j]));
                else if (scale == Scale.DARK) med[i][j] = Math.min(matR[i][j], Math.min(matG[i][j], matB[i][j]));
            }
        }
        generateImage.accept((T) med, (T) med, (T) med);
    }

    @Override
    public void dominantColor() {

    }

    @Override
    public void binaryImage() {

    }

    @Override
    public void negativeImage() {

    }

    @Override
    public void removeColor() {

    }

    @Override
    public <T> void rotate(int[][] matR, int[][] matG, int[][] matB, MatrixConsumer<T, T, T> generateImage, Double angle) {
        int originalWidth = matR[0].length, originalHeight = matR.length;
        double radians = Math.toRadians(angle);
        double cos = Math.cos(radians), sin = Math.sin(radians);

        int newWidth = (int) (Math.abs(originalWidth * cos) + Math.abs(originalHeight * sin));
        int newHeight = (int) (Math.abs(originalWidth * sin) + Math.abs(originalHeight * cos));
        int[][][] newImage = new int[3][newHeight][newWidth];

        int centerX = originalWidth / 2, centerY = originalHeight / 2;
        int newCenterX = newWidth / 2, newCenterY = newHeight / 2;

        for (int y = 0; y < originalHeight; y++) {
            for (int x = 0; x < originalWidth; x++) {
                int translatedX = x - centerX, translatedY = y - centerY;
                int newX = (int) (translatedX * cos - translatedY * sin) + newCenterX;
                int newY = (int) (translatedX * sin + translatedY * cos) + newCenterY;

                if (newX >= 0 && newX < newWidth && newY >= 0 && newY < newHeight) {
                    newImage[0][newY][newX] = matR[y][x];
                    newImage[1][newY][newX] = matG[y][x];
                    newImage[2][newY][newX] = matB[y][x];
                }
            }
        }

        generateImage.accept((T) newImage[0], (T) newImage[1], (T) newImage[2]);
    }
}
