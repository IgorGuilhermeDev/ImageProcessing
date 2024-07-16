package com.rocha.igor.usecase;

import com.rocha.igor.domain.FilterType;
import com.rocha.igor.domain.MatrixConsumer;
import com.rocha.igor.domain.Scale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageUseCaseImpl implements ImageUseCase {
    @Override
    public <T> void grayScale(int[][] matR, int[][] matG, int[][] matB, MatrixConsumer<T, T, T> generateImage, Scale scale) {
        int[][] med = new int[matR.length][matR[0].length];
        for (int i = 0; i < matR.length; i++) {
            for (int j = 0; j < matR[0].length; j++) {
                if (scale == Scale.NORMAL) med[i][j] = (matR[i][j] + matG[i][j] + matB[i][j]) / 3;
                else if (scale == Scale.LIGHT) med[i][j] = Math.max(matR[i][j], Math.max(matG[i][j], matB[i][j]));
                else if (scale == Scale.DARK) med[i][j] = Math.min(matR[i][j], Math.min(matG[i][j], matB[i][j]));
            }
        }
        generateImage.accept((T) med, (T) med, (T) med);
    }

    @Override
    public int[][] grayScale(int[][] matR, int[][] matG, int[][] matB, Scale scale) {
        int[][] med = new int[matR.length][matR[0].length];
        for (int i = 0; i < matR.length; i++) {
            for (int j = 0; j < matR[0].length; j++) {
                if (scale == Scale.NORMAL) med[i][j] = (matR[i][j] + matG[i][j] + matB[i][j]) / 3;
                else if (scale == Scale.LIGHT) med[i][j] = Math.max(matR[i][j], Math.max(matG[i][j], matB[i][j]));
                else if (scale == Scale.DARK) med[i][j] = Math.min(matR[i][j], Math.min(matG[i][j], matB[i][j]));
            }
        }
        return med;
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

    @Override
    public <T> void filter(int[][] matR, int[][] matG, int[][] matB, MatrixConsumer<T, T, T> generateImage, int kernelSize, FilterType filterType, boolean useMedian) {
        int[][] med = grayScale(matR, matG, matB, Scale.NORMAL);

        switch (filterType) {
            case MEAN -> meanAndMedian(med, kernelSize, generateImage, false);
            case SOBEL -> sobel(med, generateImage);
            case GAUSSIAN -> gaussianFilter(med, kernelSize, generateImage);
            case MEDIAN -> meanAndMedian(med, kernelSize, generateImage, true);
            default -> System.out.println("Filter not found");
        }
    }

    public <T> void meanAndMedian(int[][] med, int kernelSize, MatrixConsumer<T, T, T> generateImage,  boolean useMedian) {
        int rows = med.length;
        int cols = med[0].length;
        int[][] result = new int[rows][cols];

        for (int i = 0; i < med.length; i++) {
            for (int j = 0; j < med[0].length; j++) {
                if (useMedian) {
                    List<Integer> neighbors = getNeighbors(med, i, j, kernelSize);
                    Collections.sort(neighbors);
                    int medianValue = neighbors.get(neighbors.size() / 2);
                    result[i][j] = medianValue;
                } else result[i][j] = getNewPixelValue(med, i, j, kernelSize, null, false, false);
            }
        }
        generateImage.accept((T) result, (T) result, (T) result);
    }

    public <T> void sobel(int[][] med, MatrixConsumer<T, T, T> generateImage) {
        int rows = med.length;
        int cols = med[0].length;
        int[][] result = new int[rows][cols];

        int[][] gx = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        int[][] gy = {
                {1, 2, 1},
                {0, 0, 0},
                {-1, -2, -1},
        };

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int gradientX = getNewPixelValue(med, i, j, 3, gx, true, false);
                int gradientY = getNewPixelValue(med, i, j, 3, gy, true, false);

                int magnitude = (int) Math.sqrt(gradientX * gradientX + gradientY * gradientY);
                result[i][j] = Math.min(magnitude, 255);
            }
        }

        generateImage.accept((T) result, (T) result, (T) result);
    }

    public <T> void gaussianFilter(int[][] med, int kernelSize, MatrixConsumer<T, T, T> generateImage) {
        int rows = med.length;
        int cols = med[0].length;
        int[][] result = new int[rows][cols];

        int[][] kernel = getGaussianKernel(kernelSize);
        int halfK = kernelSize / 2;

        for (int i = halfK; i < rows - halfK; i++) {
            for (int j = halfK; j < cols - halfK; j++) {
                result[i][j] = getNewPixelValue(med, i, j, kernelSize, kernel, true, true);
            }
        }

        generateImage.accept((T) result, (T) result, (T) result);
    }

    public int getNewPixelValue(int[][] med, int row, int col, int kernelSize, int[][] kernel, boolean isKernel, boolean isGaussian) {
        int sum = 0;
        int count = 0;
        int halfK = kernelSize / 2;

        int numRows = med.length;
        int numCols = med[0].length;

        for (int i = -halfK; i <= halfK; i++) {
            for (int j = -halfK; j <= halfK; j++) {
                int neighborRow = row + i;
                int neighborCol = col + j;
                if (neighborRow >= 0 && neighborRow < numRows && neighborCol >= 0 && neighborCol < numCols) {
                    if (isKernel) {
                        sum += med[neighborRow][neighborCol] * kernel[i + halfK][j + halfK];
                        if(isGaussian){
                            count += kernel[i + halfK][j + halfK];
                        }
                    } else {
                        sum += med[neighborRow][neighborCol];
                        count++;
                    }
                }
            }
        }
        return isKernel && !isGaussian ? sum : (count > 0 ? sum / count : med[row][col]);
    }

    public int[][] getGaussianKernel(int kernelSize) {
        int[][] kernel;
        switch (kernelSize) {
            case 5 -> kernel = new int[][]{
                    {1, 4, 7, 4, 1},
                    {4, 16, 26, 16, 4},
                    {7, 26, 41, 26, 7},
                    {4, 16, 26, 16, 4},
                    {1, 4, 7, 4, 1}
            };
            case 7 -> kernel = new int[][]{
                    {0, 0, 1, 2, 1, 0, 0},
                    {0, 3, 13, 22, 13, 3, 0},
                    {1, 13, 59, 97, 59, 13, 1},
                    {2, 22, 97, 159, 97, 22, 2},
                    {1, 13, 59, 97, 59, 13, 1},
                    {0, 3, 13, 22, 13, 3, 0},
                    {0, 0, 1, 2, 1, 0, 0}
            };
            default -> kernel = new int[][]{
                    {1, 2, 1},
                    {2, 4, 2},
                    {1, 2, 1}
            };
        }
        return kernel;
    }

    private List<Integer> getNeighbors(int[][] med, int row, int col, int kernelSize) {
        List<Integer> neighbors = new ArrayList<>();
        int halfK = kernelSize / 2;

        for (int i = -halfK; i <= halfK; i++) {
            for (int j = -halfK; j <= halfK; j++) {
                int neighborRow = row + i;
                int neighborCol = col + j;
                if (isValidPixel(med, neighborRow, neighborCol)) {
                    neighbors.add(med[neighborRow][neighborCol]);
                }
            }
        }

        return neighbors;
    }

    private boolean isValidPixel(int[][] med, int row, int col) {
        return row >= 0 && row < med.length && col >= 0 && col < med[0].length;
    }

}
