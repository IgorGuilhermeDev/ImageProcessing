package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class MainPane extends JFrame {
    private JDesktopPane theDesktop;
    private int[][] matR, matG, matB;
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
        openMenu.add(fileItem);
        openMenu.add(newFrame);
        bar.add(openMenu);
    }

    private void setupProcessMenu(JMenuBar bar){
        JMenu processMenu = new JMenu("Processar");
        var grayScale = new JMenuItem("Escala de cinza");
        var binaryImage = new JMenuItem("Imagem binária");
        var negative = new JMenuItem("Negativa");
        var dominantColor = new JMenuItem("Cor dominante");
        var darkGray = new JMenuItem("Cinza escuro");
        var lightGray = new JMenuItem("Cinza claro");
        var resize = new JMenuItem("Redimensionar");
        var degree = new JMenuItem("Girar 90");

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
}
