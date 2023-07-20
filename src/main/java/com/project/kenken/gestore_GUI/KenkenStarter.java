package com.project.kenken.gestore_GUI;

public class KenkenStarter {
    private static final int DEFAULT = 4;

    public static void main(String[] args) {
        KenkenWindow kenkenWindow = new KenkenWindow(DEFAULT);
        kenkenWindow.setVisible(true);
        kenkenWindow.setResizable(false);
    }
}
