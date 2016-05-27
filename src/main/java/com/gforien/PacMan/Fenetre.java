package com.gforien.PacMan;

import java.awt.Canvas;
import javax.swing.JFrame;

/**
 * Permet l'instanciation d'une fenêtre contenant le Jeu
 *
 * @author Iane Vallanzasca
 * @author Youssef Bricha
 * @author Gabriel Forien
 */
public class Fenetre extends JFrame {

    // Windows
    public static final int HAUTEUR_DIFF = 28;
    public static final int LARGEUR_DIFF = 6;

    // Linux
    //public static final int HAUTEUR_DIFF = 37;
    //public static final int LARGEUR_DIFF = 0;

    public Fenetre(int largeur, int hauteur, String titre, Jeu jeu) {
        this.setTitle(titre);
        this.setSize(largeur+LARGEUR_DIFF, hauteur+HAUTEUR_DIFF);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // this.add(autreCanvas);
        this.add(jeu);
        this.setVisible(true);
    }
}

