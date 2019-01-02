package com.gforien.PacMan;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.Canvas;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.InterruptedException;
import java.awt.Image;
import java.awt.Color;
import java.lang.Math;

/**
 * Classe principale du programme PacMan
 * Elle instancie toutes les autres, elle définit les constantes necessaires au jeu
 * et elle contient la mainloop
 * @version 0.9
 * @author Iane Vallanzasca
 * @author Youssef Bricha
 * @author Gabriel Forien
 */
public class Jeu extends Canvas {

    public static final String TITRE = "Pac-Man";
    // chemins des images à  charger
    public static final String RES = "src/main/resources/";
    public static final String CHEMIN_PACMAN = RES+"images/PacMan";
    public static final String CHEMIN_PACMAN_0 = RES+"images/PacMan.DROITE/5.png";
    public static final String CHEMIN_FANTOME_1 = RES+"images/Fantome/ghost_blue.png";
    public static final String CHEMIN_FANTOME_2 = RES+"images/Fantome/ghost_red.png";
    public static final String CHEMIN_FANTOME_3 = RES+"images/Fantome/ghost_pink.png";
    public static final String CHEMIN_FANTOME_4 = RES+"images/Fantome/ghost_orange.png";
    public static final String CHEMIN_PBOULE = RES+"images/little_dot.png";
    public static final String CHEMIN_GBOULE = RES+"images/big_dot.png";
    public static final String MUSIQUE_DEBUT = RES+"audio/pacman_beginning.wav";
    public static final String MUSIQUE_GAGNE = RES+"audio/pacman_intermission.wav";
    public static final String MUSIQUE_PERD  = RES+"audio/pacman_eatfruit.wav";

    // le plateau sera initialisé plus tard à  partir de ce texte
    // c'est bien plus pratique que d'instancier 780 objets Case un par un
    public static final String[] PLATEAU =
   {"----------------------------------------------------",
    "-............--..........--..........--............-",
    "-.----.-----.--.--------.--.--------.--.-----.----.-",
    "-.----.-----.--.--------.--.--------.--.-----.----.-",
    "-.----.-----.--.--------.--.--------.--.-----.----.-",
    "-..................................................-",
    "-.----.--.--------.--.--------.--.--------.--.----.-",
    "-.----.--.--------.--.--------.--.--------.--.----.-",
    "-......--....--....--....--....--....--....--......-",
    "------.--.--.--.--------.--.--------.--.--.--.------",
    "     -.--.--.--.--------.--.--------.--.--.--.-     ",
    "     -.--.--.......--          --.......--.--.-     ",
    "     -.--.--.-----.--          --.-----.--.--.-     ",
    "------.--.--.-----.--          --.-----.--.--.------",
    "-     .......--......          ......--.......     -",
    "------.--.-----.-----          -----.-----.--.------",
    "     -.--.-----.-----          -----.-----.--.-     ",
    "     -.--..........--          --..........--.-     ",
    "     -.--.--------.--.--------.--.--------.--.-     ",
    "------.--.--------.--.--------.--.--------.--.------",
    "-............--..........--..........--............-",
    "-.----.-----.--.--------.--.--------.--.-----.----.-",
    "-.----.-----.--.--------.--.--------.--.-----.----.-",
    "-...--........................................--...-",
    "---.--.--.--------.--.--------.--.--------.--.--.---",
    "---.--.--.--------.--.--------.--.--------.--.--.---",
    "-......--....--....--....--....--....--....--......-",
    "-.-.--.--.--------.--.--------.--.--------.--.--.-.-",
    "-..................................................-",
    "----------------------------------------------------"};

    public static final int TAILLE_CASE = 24;
    public static final int NB_LIGNES = PLATEAU.length;
    public static final int NB_COLONNES = PLATEAU[0].length();

    public static final int HAUTEUR = NB_LIGNES*TAILLE_CASE;
    public static final int LARGEUR = NB_COLONNES*TAILLE_CASE;
    public static final int NB_CASES = NB_LIGNES*NB_COLONNES;

    // il y a, finalement, toutes les cases à  afficher + le PacMan + 4 fantômes
    public static final int NB_FANTOMES = 6;
    public static final int NB_ELEMENTS = NB_CASES + 1 + NB_FANTOMES;



    private Fenetre fen;
    private BufferStrategy bs;
    private Graphics g;
    private Element[] list;
    public static boolean gagne = false;
    public static boolean arretImmediat = false;
    public static boolean finDuJeu = false;

    public static void main(String[] args) {
        new Jeu();
    }
    public static int intervalle(int val, int min, int max) {
        if (val > max)
            return max;
        else if (val < min)
            return min;
        else
            return val;
    }

    public static Image importerImage(String chemin) {
        Image img = null;
        try {
            img = ImageIO.read(new File(chemin));
        } catch (IOException err) {
            err.printStackTrace();
            System.exit(0);
        }
        return img;
    }


    public Jeu() {
        // initialisation de la fenetre auquel le jeu est associé
        this.fen = new Fenetre(LARGEUR, HAUTEUR, TITRE, this);

        // initialisation du tableau des éléments de jeu;
        int n = 0;
        this.list = new Element[NB_ELEMENTS];

        // on ajoute à  la liste le PacMan, qu'on crée avec une position et une image
        Image pacmanImg = importerImage(CHEMIN_PACMAN_0);
        // les quatre fantà´mes de la màªme manière
        Image fantome1Img = importerImage(CHEMIN_FANTOME_1);
        Image fantome2Img = importerImage(CHEMIN_FANTOME_2);
        Image fantome3Img = importerImage(CHEMIN_FANTOME_3);
        Image fantome4Img = importerImage(CHEMIN_FANTOME_4);

        this.list[n] = new PacMan ( 1*TAILLE_CASE,  1*TAILLE_CASE, pacmanImg, this.list); n++;
        this.list[n] = new Fantome(26*TAILLE_CASE, 14*TAILLE_CASE, fantome1Img, this.list); n++;
        this.list[n] = new Fantome(28*TAILLE_CASE, 13*TAILLE_CASE, fantome1Img, this.list); n++;
        this.list[n] = new Fantome(27*TAILLE_CASE, 15*TAILLE_CASE, fantome2Img, this.list); n++;
        this.list[n] = new Fantome(26*TAILLE_CASE, 14*TAILLE_CASE, fantome3Img, this.list); n++;
        this.list[n] = new Fantome(27*TAILLE_CASE, 15*TAILLE_CASE, fantome4Img, this.list); n++;
        this.list[n] = new Fantome(25*TAILLE_CASE, 16*TAILLE_CASE, fantome4Img, this.list); n++;

        // les cases du jeu qu'on crée à  partir du PLATEAU
        Case[] p = Case.fillTab(PLATEAU);
        for (int i=0; i<p.length; i++) {
            this.list[n+i] = p[i];
        }

        // on ajoute le controleur de jeu, qui ne controlera que le pacman
        this.addKeyListener(new EntreeClavier((PacMan)list[0]));

        this.jouerMusique(MUSIQUE_DEBUT);
        //this.addKeyListener(new EntreeClavier2(this));
        this.run();
    }

    /** run - boucle principale du jeu
     *
     *  C'est la methode qui dirige toutes les autres puisque c'est elle qui gère le temps
     *  Un jeu graphique doit faire le pont entre la vitesse d'execution du programme
     *  (la vitesse a laquelle tourne une boucle while, typiquement) et la vitesse a laquelle
     *  "tourne" le jeu
     *
     *  Le programme peut s'executer trop rapidement donc on maitrise donc la vitesse a laquelle 
     *  "tourne" le jeu en definissant une frequence d'execution, et en ne declenchant des actions qu'a
     *  cette frequence
     *
     *  Le programme peut aussi s'executer trop lentement et on doit alors reflechir a l'optimisation
     *  La principale source d'optimisation dans un jeu est de dessiner les objets a l'ecran le moins
     *  possible puisque c'est une operation très coà»teuse en ressources Il faut alors identifier les
     *  objets statiques: ici, les murs du plateau, et toutes les cases sur lequelles aucun personnage 
     *  ne passe, et ne pas les dessiner tant qu'elles n'evoluent pas
     *  Mais ce jeu etant assez simple, nous n'avons eu aucun besoin d'optimiser notre code, donc nous
     *  redessinons en noir l'ecran entier, et redessinons tous les objets par-dessus, a chaque affichage
     *
     *  On fait alors une distinction entre l'affichage du jeu et l'evolution du jeu :
     *  - l'affichage doit etre aussi rapide que possible
     *  - l'evolution doit etre aussi rapide que voulue, aussi proche que possible d'une frequence definie
     *  C'est pour cela qu'on va distinguer affichage et evolution/animation pour tous les elements du jeu
     *  et que la boucle principale appelle ces deux methodes de manière asynchrone
     */
    public void run() {
        long temps0 = System.nanoTime();
        long temps0ms = System.currentTimeMillis();
        long temps = 0;

        // on définit la frequence d'animation et les FPS en Hertz
        final long frequence = 100;
        int fps = 0;
        // étant donné qu'on récupère une différence de temps en nanosecondes
        // on définit la période entre 2 animations, en nanosecondes
        final double periode = 1000000000/frequence;
        double delta = 0;

        // de plus le PacMan s'ouvre et se ferme à  une fréquence indépendante du reste du jeu
        long frequencePacMan = 50;
        double periodePacMan = 1000000000/frequencePacMan;
        double deltaPacMan = 0;

        // avoir le focus permet de récupérer les touches entrées au clavier
        // par défaut, le focus n'est pas forcément fait sur l'objet Jeu dans la fenàªtre
        // il faut donc le demander
        this.requestFocus();

        // dans le jeu c'est l'objet PacMan qui analyse l'état du jeu, et y met fin
        while (!finDuJeu) {
            temps = System.nanoTime();
            delta += temps - temps0;
            deltaPacMan += temps - temps0;
            temps0 = temps;

            if (deltaPacMan >= periodePacMan) {
                ((PacMan)this.list[0]).changerImage();
                deltaPacMan -= periodePacMan;
            }

            while (delta >= periode) {
                animer();
                delta -= periode;
            }

            afficher();

            // compteur de FPS : images par seconde
            // les FPS correspondent à  la fréquence d'exécution du programme
            // et sont donc un indice de performance du programme
            /*fps++;
            if (System.currentTimeMillis() - temps0ms >= 1000) {
                temps0ms = System.currentTimeMillis();
                System.out.println("FPS : "+frames);
                fps=0;
            }*/
        }
        if (!arretImmediat) {
            if (gagne) {
                System.out.println("Bravo ! Vous avez gagné !");
                jouerMusique(MUSIQUE_GAGNE);
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException err) {}
            } else {
                System.out.println("Vous avez perdu.");
                jouerMusique(MUSIQUE_PERD);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException err) {}
            }
            System.out.println("A bientôt !");
        }
        // on demande à  l'OS la terminaison du processus
        System.exit(0);
    }

    public static boolean jeuFini(Element[] list) {
        boolean b = true;
        for(int i=NB_FANTOMES+1; i<NB_ELEMENTS; i++) {
            // on ne teste pas une référence invalide
            if (list[i] != null && (Case)list[i] != null)
                if (((Case)list[i]).getPBoule() == true)
                    b = false;
        }
        return b;
    }
    public void jouerMusique(String chemin) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(chemin));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception err) {}
    }

    public void animer() {
        Element temp;
        for (int i=list.length-1; i>=0; i--) {
            temp = list[i];
            if (temp != null)
                temp.animer();
        }
    }

    public void afficher() {
        //  AFFICHAGE n°1
        /*/
        bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();

        g.setColor(Color.ORANGE);
        g.fillRect(0, 0, LARGEUR+10, HAUTEUR+10);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, LARGEUR, HAUTEUR);

        g.setColor(Color.WHITE); g.fillRect(0,0,TAILLE_CASE,TAILLE_CASE);
        //for (int ligne=0; ligne<PLATEAU.length; ligne++) {
        //for (int colonne=0; colonne<PLATEAU[ligne].length(); colonne++) {

        Element temp;
        for (int i=list.length-1; i>=0; i--) {
            temp = list[i];
            if (temp != null)
                temp.afficher(g);
        }

        g.dispose();
        bs.show();
        /*/

        //  AFFICHAGE n°2
        /**/
        Image img = createImage(Jeu.LARGEUR, Jeu.HAUTEUR);
        g = img.getGraphics();

        g.setColor(Color.ORANGE);
        g.fillRect(0, 0, LARGEUR+10, HAUTEUR+10);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,LARGEUR,HAUTEUR);

        Element temp;
        for (int i=list.length-1; i>=0; i--) {
            temp = list[i];
            if (temp != null)
                temp.afficher(g);
        }

        this.getGraphics().drawImage(img, 0, 0, null);
        /**/
    }
}
