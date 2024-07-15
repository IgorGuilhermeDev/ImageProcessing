package com.rocha.igor.domain;

public class Image {

    private int[][] matR;
    private int[][] matG;
    private int[][] matB;

    public Image(int[][] matR, int[][] matG, int[][] matB) {
        this.matR = matR;
        this.matG = matG;
        this.matB = matB;
    }

    public int[][] getMatR() {
        return matR;
    }

    public void setMatR(int[][] matR) {
        this.matR = matR;
    }

    public int[][] getMatG() {
        return matG;
    }

    public void setMatG(int[][] matG) {
        this.matG = matG;
    }

    public int[][] getMatB() {
        return matB;
    }

    public void setMatB(int[][] matB) {
        this.matB = matB;
    }
}
