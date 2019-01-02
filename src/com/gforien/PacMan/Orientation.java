package com.gforien.PacMan;

/**
 * Orientation définit, pour les elements comme le PacMan ou les Fantomes
 * qui se deplacent sur une ligne droite, leur direction et leur sens
 *
 * @author Iane Vallanzasca
 * @author Youssef Bricha
 * @author Gabriel Forien
 */

public enum Orientation {
    HAUT,
    BAS,
    GAUCHE,
    DROITE;

    /**
     *@return l'Orientation de même direction mais de sens opposé
     */
    public Orientation oppose() {
        switch(this) {
            case HAUT:   return BAS;
            case BAS:    return HAUT;
            case GAUCHE: return DROITE;
            case DROITE: return GAUCHE;
        }
        return null;
    }

    public String toString() {
        String s = "";
        switch(this) {
            case HAUT:
                s = "HAUT";
                break;
            case BAS:
                s = "BAS";
                break;
            case GAUCHE:
                s = "GAUCHE";
                break;
            case DROITE:
                s = "DROITE";
                break;
        }
        return s;
    }
}
