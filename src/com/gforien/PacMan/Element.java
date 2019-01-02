package com.gforien.PacMan;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Color;

/**
 * Classe définissant un élément qui sera  affiché a l'écran du jeu, qui doit
 * être traité comme tel par Jeu, et qui doit avoir un certain nombre de propriétés
 * C'est une classe mère pour les vrais éléments : PacMan, les Fantomes et les Cases
 *
 * @author Iane Vallanzasca
 * @author Youssef Bricha
 * @author Gabriel Forien
 */
public class Element {

    // chaque element de jeu a une position et peut se deplacer a une vitesse donnee
    // x et y doivent appartenir a la zone dessinee donc
    // 0 <= x <= WIDTH et 0 <= y <= HEIGHT
    // de plus on definit 0 <= vit <= 10
    protected int x, y, vit=0;
    protected int taille;
    protected Id id;
    protected Image img;
    protected Color couleur;

    public Element(int x, int y, int taille, Id id, Color couleur, Image img) {
        this.taille = taille;
        this.setX(x);
        this.setY(y);
        this.id = id;
        this.couleur = couleur;
        this.img = img;
    }
    public Element(int x, int y, int taille, Id id, Color couleur) {
        this(x, y, taille, id, couleur, null);
    }
    public Element(int x, int y, int taille, Id id, Image img) {
        this(x, y, taille, id, null, img);
    }

    public void animer() {}
    public void afficher(Graphics g) {}

    // Getters
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getVit() {
        return this.vit;
    }
    public int getTaille() {
        return this.taille;
    }
    public Id getId() {
        return this.id;
    }
    public Image getImg() {
        return this.img;
    }
    public Color getCouleur() {
        return this.couleur;
    }

    // Setters
    public void setX(int val) {
        this.x = Jeu.intervalle(val, 0, Jeu.LARGEUR-this.taille);
    }
    public void setY(int val) {
        this.y =  Jeu.intervalle(val, 0, Jeu.HAUTEUR-this.taille);
    }
    public void setVit(int val) {
        this.vit =  Jeu.intervalle(val, 0, 10);
    }
    public void setImg(Image val) {
    if(this.img != null)
            this.img = val;
    }
    public void setCouleur(Color val) {
        this.couleur = val;
    }
}

