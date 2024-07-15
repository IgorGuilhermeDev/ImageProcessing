package com.rocha.igor.ui;

import com.rocha.igor.domain.Image;
import com.rocha.igor.domain.Scale;
import com.rocha.igor.usecase.ImageUseCase;
import com.rocha.igor.usecase.ImageUseCaseImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class MainPane extends JFrame {
    private JDesktopPane theDesktop;
    private int[][] matR, matG, matB;

    private ImageUseCase useCase = new ImageUseCaseImpl();
    JFileChooser fc  = new JFileChooser();
    String path = "";

    public MainPane(){
        super("Processamento De Imagens");
        var bar = new JMenuBar();
        setupMainMenus(bar);
        setJMenuBar(bar);
        theDesktop = new JDesktopPane();
        getContentPane().add(theDesktop);
        setSize(600, 400);
        setVisible(true);
    }

    private void setupMainMenus(JMenuBar bar){
        setupOpenMenu(bar);
        setupProcessMenu(bar);
        setupRemoveMenu(bar);
        setupChooseMenu(bar);
        setupIdentifyMenu(bar);
    }
    private void setupOpenMenu(JMenuBar bar){
        JMenu openMenu = new JMenu("Abrir");
        var fileItem = new JMenuItem("Abir uma imagem de arquivo");
        var newFrame = new JMenuItem("Internal Frame");
        fileItem.addActionListener(e -> {
            int result = fc.showOpenDialog(null);
            if(result == JFileChooser.CANCEL_OPTION){
                return;
            }
            path = fc.getSelectedFile().getAbsolutePath();

            JInternalFrame frame = new JInternalFrame("Exemplo", true,
                    true, true, true);
            Container container = frame.getContentPane();
            MyJPanel panel = new MyJPanel();
            container.add(panel, BorderLayout.CENTER);

            frame.pack();
            theDesktop.add(frame);
            frame.setVisible(true);
            updateMatrix();
        });
        openMenu.add(fileItem);
        openMenu.add(newFrame);
        bar.add(openMenu);
    }

    private void setupProcessMenu(JMenuBar bar){
        JMenu processMenu = new JMenu("Processar");
        var grayScale = new JMenuItem("Escala Cinza");
        var binaryImage = new JMenuItem("Imagem binária");
        var negative = new JMenuItem("Negativa");
        var dominantColor = new JMenuItem("Cor dominante");
        var darkGray = new JMenuItem("Cinza escuro");
        var lightGray = new JMenuItem("Cinza claro");
        var resize = new JMenuItem("Redimensionar");
        var degree = new JMenuItem("Girar 90");

        setupGrayScaleListeners(grayScale, darkGray, lightGray);
        setupRotateListener(degree);
        processMenu.add(grayScale);
        processMenu.add(binaryImage);
        processMenu.add(negative);
        processMenu.add(dominantColor);
        processMenu.add(darkGray);
        processMenu.add(lightGray);
        processMenu.add(resize);
        processMenu.add(degree);

        bar.add(processMenu);
    }

    private void setupRemoveMenu(JMenuBar bar){
        var removeMenu = new JMenu("Remover");
        var removeRED = new JMenuItem("Vermelha");
        var removeGREEN = new JMenuItem("Verde");
        var removeBLUE = new JMenuItem("Azul");
        removeMenu.add(removeRED);
        removeMenu.add(removeGREEN);
        removeMenu.add(removeBLUE);
        bar.add(removeMenu);
    }

    private void setupChooseMenu(JMenuBar bar){
        var chooseMenu = new JMenu("Escolher");
        var chooseRED = new JMenuItem("Vermelha");
        var chooseGREEN = new JMenuItem("Verde");
        var chooseBLUE = new JMenuItem("Azul");
        chooseMenu.add(chooseRED);
        chooseMenu.add(chooseGREEN);
        chooseMenu.add(chooseBLUE);
        bar.add(chooseMenu);

    }

    private void setupIdentifyMenu(JMenuBar bar){
        var identify = new JMenu("Identificar");
        var pen = new JMenuItem("Caneta");
        identify.add(pen);
        bar.add(identify);
    }
    class MyJPanel extends JPanel{
        private ImageIcon imageIcon;

        public MyJPanel(){
            imageIcon = new ImageIcon(path);
        }

        public void setImageIcon(ImageIcon i){
            imageIcon = i;
        }

        public void paintComponent(Graphics g){
            super.paintComponents(g);
            imageIcon.paintIcon(this, g, 0, 0);
        }

        public Dimension getPreferredSize(){
            return new Dimension(imageIcon.getIconWidth(),
                    imageIcon.getIconHeight());
        }

    }

    public Vector<int[][]> getMatrixRGB(){
        BufferedImage img;
        int[][] rmat = null;
        int[][] gmat = null;
        int[][] bmat = null;
        try {
            img = ImageIO.read(new File(path));
            rmat = new int[img.getHeight()][img.getWidth()];
            gmat = new int[img.getHeight()][img.getWidth()];
            bmat = new int[img.getHeight()][img.getWidth()];
            for(int i = 0; i < img.getHeight(); i++){
                for(int j = 0; j < img.getWidth(); j++){
                    rmat[i][j] = getPixelData(img, j, i)[0];
                    gmat[i][j] = getPixelData(img, j, i)[1];
                    bmat[i][j] = getPixelData(img, j, i)[2];

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Vector<int[][]> rgb = new Vector<int[][]>();
        rgb.add(rmat);
        rgb.add(gmat);
        rgb.add(bmat);

        return rgb;
    }

    private void geraImagem(int matrix1[][], int matrix2[][], int matrix3[][])
    {
        int[] pixels = new int[matrix1.length * matrix1[0].length*3];
        BufferedImage image = new BufferedImage(matrix1[0].length, matrix1.length, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = image.getRaster();
        int pos =0;
        for(int i =0; i < matrix1.length; i++){
            for(int j = 0; j < matrix1[0].length; j++){
                pixels[pos] = matrix1[i][j];
                pixels[pos+1] = matrix2[i][j];
                pixels[pos+2] = matrix3[i][j];
                pos+=3;
            }
        }
        raster.setPixels(0, 0, matrix1[0].length, matrix1.length, pixels);

        //Abre Janela da imagem
        JInternalFrame frame = new JInternalFrame("Processada", true,
                true, true, true);
        Container container = frame.getContentPane();
        MyJPanel panel = new MyJPanel();
        panel.setImageIcon(new ImageIcon(image));
        container.add(panel, BorderLayout.CENTER);

        frame.pack();
        theDesktop.add(frame);
        frame.setVisible(true);

    }
    
    private static int[] getPixelData(BufferedImage img, int x, int y) {
        int argb = img.getRGB(x, y);

        int rgb[] = new int[] {
                (argb >> 16) & 0xff, //red
                (argb >>  8) & 0xff, //green
                (argb      ) & 0xff  //blue
        };
        return rgb;
    }

   private <T extends JMenuItem> void setupGrayScaleListeners(T grayScale, T darkGrayScale, T lightGrayScale){
       grayScale.addActionListener(e -> {
           Image image = getRGBImage();
           this.useCase.grayScale(image.getMatR(), image.getMatG(), image.getMatB(), this::geraImagem, Scale.NORMAL);
       });

       darkGrayScale.addActionListener(e -> {
           Image image = getRGBImage();
           this.useCase.grayScale(image.getMatR(), image.getMatG(), image.getMatB(), this::geraImagem, Scale.DARK);
       });

       lightGrayScale.addActionListener(e -> {
           Image image = getRGBImage();
           this.useCase.grayScale(image.getMatR(), image.getMatG(), image.getMatB(), this::geraImagem, Scale.LIGHT);
       });
   }

   private <T extends JMenuItem> void setupRotateListener(T rotate){
       rotate.addActionListener(e -> {
           String input = JOptionPane.showInputDialog(null, "Digite o ângulo de rotação:");
           try {
               double angle = Double.parseDouble(input);
               Image image = getRGBImage();
               this.useCase.rotate(image.getMatR(), image.getMatG(), image.getMatB(), this::geraImagem, angle);
           } catch (NumberFormatException ex) {
               JOptionPane.showMessageDialog(null, "Ângulo inválido!");
           }});
       }

   private void updateMatrix(){
       Vector<int[][]> rgbMat = getMatrixRGB();
       matR = rgbMat.elementAt(0);
       matG = rgbMat.elementAt(1);
       matB = rgbMat.elementAt(2);
   }

   private Image getRGBImage(){
       return new Image(matR, matG, matB);
   }

//   private void hsv(int red[][], int blue[][], int green[][], int newRED[][], int newBLUE[][], int newGREEN[][], int type){
//        //O type server para indentifcar saturação, hue ou v
//       for (int i = 0; i < red.length; i++) {
//           for (int j = 0; j < red[0].length ; j++) {
//               double v  = Math.max(Math.max(red[i][j], green[i][j]), blue[i][j]);
//               if(type == 2) v = 255;
//               double min = Math.min(Math.min(red[i][j], green[i][j]), blue[i][j]);
//               double s = (v - min) / v;
//               if(type == 0) s = 1;
//
//
//               double h = 0;
//               if (s != 0) {
//                   if (v == red[i][j]) {
//                       h = 60 * (((green[i][j] - blue[i][j]) / (v - min)) + 0);
//                   } else if (v == green[i][j]) {
//                       h = 60 * (((blue[i][j] - red[i][j]) / (v - min)) + 2);
//                   } else {
//                       h = 60 * (((red[i][j] - green[i][j]) / (v - min)) + 4);
//                   }
//               }
//               if(h < 0) h += 0;
//               if(type == 1) h = 239;
//               double c = v * s;
//               double m = v - c;
//               double x = c * (1 - Math.abs((h / 60) % 2 - 1));
//
//               if (h >= 0 && h < 60) {
//                   newRED[i][j] = (int) (c + m);
//                   newGREEN[i][j] = (int) (x + m);
//                   newBLUE[i][j] = (int) m;
//               }
//               else if(h >= 60 && h < 120){
//                   newRED[i][j] = (int) (x + m);
//                   newGREEN[i][j] = (int) (c + m);
//                   newBLUE[i][j] = (int) m;
//               }
//               else if(h >= 120 && h < 180) {
//                   newRED[i][j] = (int) m;
//                   newGREEN[i][j] = (int) (c + m);
//                   newBLUE[i][j] = (int) (x + m);
//               }
//               else if(h >= 180 && h < 240) {
//                   newRED[i][j] = (int) m;
//                   newGREEN[i][j] = (int) (x + m);
//                   newBLUE[i][j] = (int) (c + m);
//               }else if(h >= 240 && h < 300) {
//                   newRED[i][j] = (int) (x + m);
//                   newGREEN[i][j] = (int) m;
//                   newBLUE[i][j] = (int) (c + m);
//               }else if(h >= 300 && h <= 360) { //Só para a imagem não ficar cinza eu adiconei o =
//                   newRED[i][j] = (int) (c + m);
//                   newGREEN[i][j] = (int) m;
//                   newBLUE[i][j] = (int) (x + m);
//               }
//               else {
//                   newRED[i][j] = (int) m;
//                   newGREEN[i][j] = (int) m;
//                   newBLUE[i][j] = (int) (m);
//               }
//
//               newRED[i][j] = Math.min(255, Math.max(0, newRED[i][j]));
//               newGREEN[i][j] = Math.min(255, Math.max(0, newGREEN[i][j]));
//               newBLUE[i][j] = Math.min(255, Math.max(0, newBLUE[i][j]));
//           } } }

}
