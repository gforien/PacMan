package com.gforien.PacMan;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;

/**
 * Cases du jeu, petits rectangles sur lesquels bougent ou auquels se heurtent
 * PacMan et les Fantomes
 *
 * @author Iane Vallanzasca
 * @author Youssef Bricha
 * @author Gabriel Forien
 */
public class  Case extends Element {

    private boolean pBoule;
    private boolean gBoule;

    public Case(int x, int y, Id id, Color couleur, Image img) {
        super(x, y, Jeu.TAILLE_CASE, id, couleur, img);
    }

    public void afficher(Graphics g) {
        g.setColor(couleur);
        g.fillRect(x, y, Jeu.TAILLE_CASE, Jeu.TAILLE_CASE);
        if (img != null && (pBoule || gBoule)) {
            g.drawImage(img, x, y, null);
        }
    }

    public boolean getPBoule() {
        return this.pBoule;
    }
    public boolean getGBoule() {
        return this.gBoule;
    }
    public void setPBoule(boolean val) {
        this.pBoule = val;
    }
    public void setGBoule(boolean val) {
        this.gBoule = val;
    }


    public static Case[] fillTab(String[] T){
        int hauteur = T.length;
        int largeur = T[0].length();
        Case[] tFinal = new Case[hauteur*largeur];
        String t = "";
        char c = ' ';
        int n = 0;
        int x = 0;
        int y = 0;

        Image imgpboule = Jeu.importerImage(Jeu.CHEMIN_PBOULE);
        Image imggboule = Jeu.importerImage(Jeu.CHEMIN_GBOULE);

        //System.out.println(hauteur + " " + largeur);

        for(int i=0; i<T.length; i++){
            t=T[i];
            for(int j=0; j<T[i].length(); j++){
                c = T[i].charAt(j);
                switch (c) {
                    // case vide
                    case ' ':
                        tFinal[n] = new Case(x, y, Id.CASE, Color.BLACK, null);
                        n++;
                        break;
                    // mur
                    case '-':
                        tFinal[n] = new Case(x, y, Id.MUR, Color.BLUE, null);
                        n++;
                        break;
                    // petite boule
                    case '.':
                        tFinal[n] = new Case(x, y, Id.CASE, Color.BLACK, imgpboule);
                        tFinal[n].setPBoule(true);
                        n++;
                        break;
                    // grosse boule
                    case ',':
                        tFinal[n] = new Case(x, y, Id.CASE, Color.BLACK, imggboule);
                        tFinal[n].setGBoule(true);
                        n++;
                        break;
                }

                x += Jeu.TAILLE_CASE;
                if (x == Jeu.LARGEUR) {
                    x = 0;
                    y = y + Jeu.TAILLE_CASE;
                }
            }
        }

        return tFinal;
    }
}
