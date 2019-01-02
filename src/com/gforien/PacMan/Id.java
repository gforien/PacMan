package com.gforien.PacMan;

/**
 * Type associe a chaque Element du jeu, permettant aux autres elements
 * de l'identifier comme tel
 *
 * @author Iane Vallanzasca
 * @author Youssef Bricha
 * @author Gabriel Forien
 */
public enum Id {
    PACMAN,
    FANTOME,
    CASE,
    MUR;

    public String toString() {
        String s = "";
        switch(this) {
            case PACMAN:
                s = "PACMAN";
                break;
            case FANTOME:
                s = "FANTOME";
                break;
            case CASE:
                s = "CASE";
                break;
            case MUR:
                s = "MUR";
                break;
        }
        return s;
    }
}
