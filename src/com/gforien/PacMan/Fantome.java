package com.gforien.PacMan;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;

/**
 * @author Iane Vallanzasca
 * @author Youssef Bricha
 * @author Gabriel Forien
 */
public class Fantome extends Element {

    Element[] list;
    Orientation orient;
    boolean bloque = false;

    public Fantome(int x, int y, Image img, Element[] list) {
        super(x, y, Jeu.TAILLE_CASE, Id.FANTOME, img);
        this.setVit(4);
        this.orient = Orientation.GAUCHE;
        this.list = list;
        randirection();
    }

    public void animer(){
        Element eAtteint = collisionMur(orient);
        if (eAtteint != null){
        randirection();
    }

        if(!bloque) {
            switch (orient){
                case HAUT:
                    setY(y-vit);
                    break;
                case BAS:
                    setY(y+vit);
                    break;
                case DROITE:
                    setX(x+vit);
                    break;
                case GAUCHE:
                    setX(x-vit);
                    break;
            }
        }
    }

    public Element collisionMur(Orientation oRecherchee) {
        int deltaX = 0, deltaY = 0;

        for(int i=1; i<list.length; i++) {

            if(list[i] == null || list[i].getId() != id.MUR)
                continue;

            deltaX = list[i].getX() - x;
            deltaY = list[i].getY() - y;

            switch(oRecherchee) {
                case HAUT:
                    if(deltaX == 0 && deltaY == -Jeu.TAILLE_CASE) return list[i];
                    else break;
                case BAS:
                    if(deltaX == 0 && deltaY == Jeu.TAILLE_CASE) return list[i];
                    else break;
                case GAUCHE:
                    if(deltaY == 0 && deltaX == -Jeu.TAILLE_CASE) return list[i];
                    else break;
                case DROITE:
                    if(deltaY == 0 && deltaX == Jeu.TAILLE_CASE) return list[i];
                    else break;
            }
        }
        return null;
    }


    public void randirection(){
        int dir = (int)(4*Math.random());
        Element haut = null, bas = null, gauche = null, droite = null;
        switch (orient){
            case HAUT:
                droite = collisionMur (Orientation.DROITE);
                gauche = collisionMur (Orientation.GAUCHE);
                if (droite!=null && gauche==null){
                    if (dir<2){
                        orient = Orientation.GAUCHE;
                    }else {
                        orient = Orientation.BAS;
                    }
                } else if (gauche!=null && droite==null){
                    if (dir<2){
                        orient = Orientation.DROITE;
                    }else {
                        orient = Orientation.BAS;
                    }
                } else if (gauche!=null && droite!=null){
                    orient = Orientation.BAS;
                } else {
                    if (dir<2){
                        orient=Orientation.BAS;
                    } else if (dir>=2 && dir<3){
                        orient=Orientation.GAUCHE;
                    } else {
                        orient=Orientation.DROITE;
                    }
                }
                break;

            case BAS:
                droite = collisionMur (Orientation.DROITE);
                gauche = collisionMur (Orientation.GAUCHE);
                if (droite!=null && gauche==null){
                    if (dir<2){
                        orient = Orientation.GAUCHE;
                    }else {
                        orient = Orientation.HAUT;
                    }
                } else if (gauche!=null && droite==null){
                    if (dir<2){
                        orient = Orientation.DROITE;
                    }else {
                        orient = Orientation.HAUT;
                    }
                } else if (gauche!=null && droite!=null){
                    orient = Orientation.HAUT;
                } else {
                    if (dir<2){
                        orient=Orientation.HAUT;
                    } else if (dir>=2 && dir<3){
                        orient=Orientation.GAUCHE;
                    } else {
                        orient=Orientation.DROITE;
                    }
                }
                break;

            case DROITE:
                haut = collisionMur (Orientation.HAUT);
                bas = collisionMur (Orientation.BAS);
                if (haut!=null && bas==null){
                    if (dir<2){
                        orient = Orientation.GAUCHE;
                    }else {
                        orient = Orientation.BAS;
                    }
                } else if (bas!=null && haut==null){
                    if (dir<2){
                        orient = Orientation.GAUCHE;
                    }else {
                        orient = Orientation.HAUT;
                    }
                } else if (haut!=null && bas!=null){
                    orient = Orientation.GAUCHE;
                } else {
                    if (dir<2){
                        orient=Orientation.HAUT;
                    } else if (dir>=2 && dir<3){
                        orient=Orientation.GAUCHE;
                    } else {
                        orient=Orientation.BAS;
                    }
                }
                break;

            case GAUCHE:
                haut = collisionMur (Orientation.HAUT);
                bas = collisionMur (Orientation.BAS);
                if (haut!=null && bas==null){
                    if (dir<2){
                        orient = Orientation.DROITE;
                    }else {
                        orient = Orientation.BAS;
                    }
                } else if (bas!=null && haut==null){
                    if (dir<2){
                        orient = Orientation.DROITE;
                    }else {
                        orient = Orientation.HAUT;
                    }
                } else if (haut!=null && bas!=null){
                    orient = Orientation.DROITE;
                }else {
                    if (dir<2){
                        orient=Orientation.HAUT;
                    } else if (dir>=2 && dir<3){
                        orient=Orientation.DROITE;
                    } else {
                        orient=Orientation.BAS;
                    }
                }
                break;

        }
    }


    public Orientation getOrient() {
        return this.orient;
    }
    public void afficher(Graphics g) {
        if(img != null) {
            g.drawImage(img, x, y, null);
        }
    }

    public void setBloque(boolean val) {
        this.bloque = val;
    }
}
