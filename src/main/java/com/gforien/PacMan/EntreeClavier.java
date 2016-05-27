package com.gforien.PacMan;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Classe permettant le controle du PacMan au clavier
 *
 * @author Iane Vallanzasca
 * @author Youssef Bricha
 * @author Gabriel Forien
 */
public class EntreeClavier extends KeyAdapter {

    private PacMan pacman;

    public EntreeClavier(PacMan pacman) {
        this.pacman = pacman;
    }

    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();

        // si le PacMan ne bouge plus à  cause d'un Mur en face de lui, il peut changer de
        // direction et ne plus être bloqué
        //
        // si le PacMan ne bouge plus à  cause d'un Fantome, le Fantome en question le
        // REbloquera instantanément
        pacman.setBloque(false);

        switch(key) {
            case KeyEvent.VK_UP:
                pacman.setProchainMouv(Orientation.HAUT);
                break;
            case KeyEvent.VK_DOWN:
                pacman.setProchainMouv(Orientation.BAS);
                break;
            case KeyEvent.VK_LEFT:
                pacman.setProchainMouv(Orientation.GAUCHE);
                break;
            case KeyEvent.VK_RIGHT:
                pacman.setProchainMouv(Orientation.DROITE);
                break;
            case KeyEvent.VK_M:
                // couper la musique
                break;
            case KeyEvent.VK_Q:
                Jeu.arretImmediat = true;
                Jeu.finDuJeu = true;
                break;
            case KeyEvent.VK_ESCAPE:
                Jeu.arretImmediat = true;
                Jeu.finDuJeu = true;
                break;
        }
    }

    public void keyReleased(KeyEvent event) {}
    public void keyTyped(KeyEvent event) {}
}

