package com.gforien.PacMan;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;

/**
 * Element principal du jeu, controlé par le joueur
 *
 * @author Iane Vallanzasca
 * @author Youssef Bricha
 * @author Gabriel Forien
 */
public class PacMan extends Element {

    private Element[] list;
    private Orientation orient;
    private Orientation prochainMouv;
    private Image[][] images;
    private int numImg = 5;
    private int nbImg = 10;
    private boolean ferme = true;
    private boolean bloque = false;

    public PacMan(int x, int y, Image img, Element[] list) {
        super(x, y, Jeu.TAILLE_CASE, Id.PACMAN, img);
        this.orient = Orientation.DROITE;
        this.prochainMouv = Orientation.DROITE;
        this.setVit(2);
        this.list = list;

        // Récuperer images
        String chemin;
        images = new Image[nbImg][4];
        for(int i=0; i<nbImg; i++) {
            for (int j=0; j<4; j++){
            if (j==0){
                chemin=Jeu.CHEMIN_PACMAN+".HAUT/"+i+".png";
            } else if (j==1){
                chemin=Jeu.CHEMIN_PACMAN+".BAS/"+i+".png";
            } else if (j==2){
                chemin=Jeu.CHEMIN_PACMAN+".GAUCHE/"+i+".png";
            } else {
                chemin=Jeu.CHEMIN_PACMAN+".DROITE/"+i+".png";
            }
            images[i][j] = Jeu.importerImage(chemin);
            }
        }
    }

    public void animer() {
        Element eAtteint;

        eAtteint = collision(Id.FANTOME); 
        if (eAtteint != null) {
            bloque = true;
            ((Fantome)eAtteint).setBloque(true);
            Jeu.finDuJeu = true;
        }

        if(x%Jeu.TAILLE_CASE == 0 && y%Jeu.TAILLE_CASE == 0) {
            // collision avec une Case, on stocke sa référence pour pouvoir supprimer
            // sa petite boule
            eAtteint = collision(Id.CASE); 
            if (eAtteint != null) {
                ((Case)eAtteint).setPBoule(false);
                if (Jeu.jeuFini(list)) {
                    Jeu.gagne = true;
                    Jeu.finDuJeu = true;
                }
            }

            // collision avec un mur, inutile de stocker la référence de l'élement atteint
            if (collision(Id.MUR) != null) {
                // on change directement de direction si cela n'entraine pas de collision
                // sinon, le PacMan est bloqué
                if (collisionMur(prochainMouv) == null) {
                    orient = prochainMouv;
                }
                else {
                    bloque = true;
                }
            }

            // s'il n'y a pas de collision potentielle en changeant de direction, on le fait
            // cela permet d'avoir un prochainMouv, que PacMan arrive à  un croisement oà¹ il
            // pourrait continuer tout droit, mais qu'il tourne selon le prochainMouv spécifié
            if (collisionMur(prochainMouv) == null) {
                orient = prochainMouv;
            }
        }


        if (!bloque) {
            switch(orient) {
                case HAUT:   setY(getY()-vit); break;
                case BAS:    setY(getY()+vit); break;
                case GAUCHE: setX(getX()-vit); break;
                case DROITE: setX(getX()+vit); break;
            }
        }
    }

    /** collision
     *  Cette methode est d'une extreme importance puisqu'elle determine l'interaction du PacMan
     *  avec le reste des elements du jeu.Elle est appelee a chaque animation, donc a la frequence
     *  d'execution du jeu definie, et teste des correspondances de position entre le PacMan et
     *  les elements du jeu, ~800 elements.Elle doit donc parcourir la liste des elements du jeu
     *  et faire une reponse le plus rapidement possible
     *
     *  Le design de cette methode est donc axe sur la rapidite d'execution, on boucle sur la liste des
     *  elements et on passe au prochain element a la premiere occasion.
     *  On utilise donc les Id pour cela :
     *  -les collisions avec les fantomes peuvent se faire n'importe quand, mais il y a un nombre defini 
     *  de Fantomes : quand on a teste les n Fantomes, on peut forcement retourner null
     *  - les collisions avec les murs ou les Cases n'arrivent que quand le PacMan est "aligne" avec les 
     *  Cases. Donc on choisira un moment precis pour appeler collision(id.CASE)
     *
     *  Le plus gros travail de cette methode est de boucler sur toutes les Cases (qui peuvent etre des murs),
     *  et par le fait que nous avons un tableau ordonne d'Element, on pourrait en fait coder une methode qui
     *  deduirait l'index d'une Case d'apres sa position. Il suffirait alors, a chaque fois que le PacMan se
     *  trouve bien centre sur une case, d'appeler cette methode en specifiant une position au-dessus,
     *  en-dessous, a gauche ou a droite du PacMan, et de verifier que l'element de liste a l'index recu
     *  n'est pas un mur.
     *  Dans notre cas:
     *                      int index  = 5 + pacman.getX() + pacMan.getY()*NB_COLONNES
     *                      Element caseActuelle = liste[index]
     *                      int index2 = 5 + pacman.getX() + (pacMan.getY()-1)*NB_COLONNES
     *                      Element caseAuDessus = liste[index2]
     *                      etc...
     *
     *  N'ayant pas de probleme de performances, on se permet de garder une methode qui gere des collisions
     *  entre PacMan et les cases en temps reel, meme si ce sont des collisions previsibles.
     *  On se trouve dans un cas particulier, mais on fait en sorte de ne pas avoir une structure de code qui 
     *  se reposerait trop sur cela. Ainsi si l'on voulait placer tous nos Element dans autre chose 
     *  qu'un tableau Element[] (ex: une LinkedList), on ne remettrait pas en cause l'integrite de notre code
     * @param  idRecherche de l'élément avec lequel on recherche une interaction
     * @return la reference du Fantome ou de la Case avec lequel PacMan rentre en contact
     */
    public Element collision(Id idRecherche) {
        int deltaX = 0, deltaY = 0;
        int compteurFantome = 0;
        for(int i=1; i<list.length; i++) {

            if(list[i] == null || list[i].getId() != idRecherche)
                continue;
                // on passe directement à  l'itération suivante

            deltaX = list[i].getX() - x;
            deltaY = list[i].getY() - y;

            switch(idRecherche) {
                // la collision PacMan - Fantome ne doit pas dépendre de l'orientation du PacMan
                case FANTOME:
                    if(deltaX == 0) {
                        if(deltaY == Jeu.TAILLE_CASE || deltaY == -Jeu.TAILLE_CASE)
                            return list[i];
                    } else if(deltaY == 0) {
                        if(deltaX == Jeu.TAILLE_CASE || deltaX == -Jeu.TAILLE_CASE)
                            return list[i];
                    } else {
                        compteurFantome++;
                        if (compteurFantome == Jeu.NB_FANTOMES)
                            return null;
                        break;
                    }
                // au contraire, il n'y a collision avec un mur que si le PacMan est orienté vers
                // le mur
                case MUR:
                    switch(orient) {
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
                // enfin la collision avec une case se fait quand le PacMan recouvre entià¨rement
                // la case : ainsi il "mange" les petites boules quand il passe dessus et non
                // quand il rencontre le bord de la case
                case CASE:
                    if(deltaX == 0 && deltaY == 0) return list[i];
                    else break;
            }
        }
        return null;
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

    public void afficher(Graphics g) {
        if(img != null) {
            g.drawImage(img, x, y, null);
        }
    }

    public void changerImage() {
        if (ferme)
            numImg--;
        else
            numImg++;
        if (numImg == 0)
            ferme = false;
        else if (numImg == nbImg-1)
            ferme = true;
        switch (orient){
            case HAUT:
                img = images[numImg][0];
                break;
            case BAS:
                img = images[numImg][1];
                break;
            case GAUCHE:
                img = images[numImg][2];
                break;
            case DROITE:
                img = images[numImg][3];
                break;
        }
    }

    public Orientation getOrient() {
        return this.orient;
    }   
    public Orientation getProchainMouv() {
        return this.prochainMouv;
    }
    public boolean getBloque() {
        return this.bloque;
    }
    public void setOrient(Orientation val) {
        this.orient = val;
    }
    public void setProchainMouv(Orientation val) {
        // si l'on veut seulement se retourner, il n'y a pas besoin de prévoir
        // un prochain mouvement et des vérifications compliquées, on peut toujours
        // se retourner
        if(this.orient.oppose() == val) {
            this.orient = val;
            this.prochainMouv = val;
        } else {
            this.prochainMouv = val;
        }
    }
    public void setBloque(boolean val) {
        this.bloque = val;
    }
}

