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
        int width = matR[0].length;
        int height = matR.length;
        double radians = Math.toRadians(angle);

        int newWidth = (int) (Math.abs(width * Math.cos(radians)) + Math.abs(height * Math.sin(radians)));
        int newHeight = (int) (Math.abs(width * Math.sin(radians)) + Math.abs(height * Math.cos(radians)));

        int[][] newRed = new int[newHeight][newWidth];
        int[][] newGreen = new int[newHeight][newWidth];
        int[][] newBlue = new int[newHeight][newWidth];

        int centerX = width / 2;
        int centerY = height / 2;
        int newCenterX = newWidth / 2;
        int newCenterY = newHeight / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int newX = (int) ((x - centerX) * Math.cos(radians) - (y - centerY) * Math.sin(radians) + newCenterX);
                int newY = (int) ((x - centerX) * Math.sin(radians) + (y - centerY) * Math.cos(radians) + newCenterY);

                if (newX >= 0 && newX < newWidth && newY >= 0 && newY < newHeight) {
                    newRed[newY][newX] = matR[y][x];
                    newGreen[newY][newX] = matG[y][x];
                    newBlue[newY][newX] = matB[y][x];
                }
            }
        }
        generateImage.accept((T) newRed, (T) newGreen, (T) newBlue);
    }
}
