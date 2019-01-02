PacMan (API Java 2D)
=================
[![Release](https://img.shields.io/github/release/gforien/pacman.svg)](https://github.com/gforien/PacMan/releases)
[![Build Status](https://travis-ci.org/gforien/PacMan.svg?branch=master)](https://travis-ci.org/gforien/PacMan)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/699010f64cc548d89e8c333bcfb419c6)](https://www.codacy.com/app/kyoto-public/PacMan?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=gforien/PacMan&amp;utm_campaign=Badge_Grade)


## Cycle de vie du projet en CLI
Une des difficultés dans ce projet a été le fait d'inclure des images et des musiques, alors que tout le projet était à chaque étape compilé à la main, dans un terminal. Dans quel dossier placer les ressources ? Quel chemin spécifier dans le code source ? Comment s'assurer que les ressources seraient trouvées à l'exécution ? Nous avons été confrontés à ces questions et avons tenté d'y répondre de la manière la plus simple possible.

Hiérarchie originale des dossiers (identique à celle obtenue après un `$ git clone`) : 
```
└───src
    ├───com
    │   └───gforien
    │       └───PacMan
    │           └─── *.java
    └───resources
        ├───audio
        └───images
```


### 1. Gestion des ressources dans le code Java

Les chemins vers les ressources sont donc des chemins absolus définies dans la classe principale **com.gforien.PacMan.Jeu**. On les chargera en passant ces ressources en paramètres aux méthodes *importerImage* ou *jouerMusique* de la classe principale.
```java
import java.net.URL;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;

public class Jeu extends Canvas {

    public static final String RES = "/resources";
    public static final String CHEMIN_PACMAN_0 = RES+"/images/PacMan.DROITE/5.png";
    public static final String CHEMIN_PBOULE = RES+"/images/little_dot.png";
    public static final String MUSIQUE_DEBUT = RES+"/audio/pacman_beginning.wav";

    public Jeu() {
        Image pacmanImg = importerImage(CHEMIN_PACMAN_0);
        this.jouerMusique(MUSIQUE_DEBUT);
```

Dans ces méthodes, on charge les ressources d'abord comme des URL, puis avec les méthodes propres aux extensions Java *imageio* et *sound*.
```java
    public void jouerMusique(String chemin) {
        URL url = null;
        AudioInputStream audioInputStream = null;
        try {
            url = Jeu.class.getResource(chemin);
            audioInputStream = AudioSystem.getAudioInputStream(url);

...

    public static Image importerImage(String chemin) {
        URL url = null;
        Image img = null;
        try {
            url = Jeu.class.getResource(chemin);
            img = ImageIO.read(url);
```


### 2. Gestion des ressources lors de la compilation et l'exécution

Nous allons compiler le code source dans un dossier *bin/*. La hiérarchie de type com.gforien.*etc* sera automatiquement recréée, il nous suffira donc ensuite de copier notre dossier de ressources dans le dossier *bin/*.
```
~/pacman $ mkdir bin
~/pacman $ javac -d bin/ src/com/gforien/PacMan/*.java
~/pacman $ cp -R src/resources/ bin/
```

On obtient la même hiérarchie dans *bin/* que dans *src/* : 
```
├───bin
│   ├───com
│   │   └───gforien
│   │       └───PacMan
│   │           └─── *.class
│   └───resources
│
└───src
    ├───com
    │   └───gforien
    │       └───PacMan
    │           └─── *.java
    └───resources
```

On peut alors exécuter simplement en spécifiant le classpath.
```
~/pacman $ java -cp bin com.gforien.PacMan.Jeu
```

### 3. Gestion des ressources lors de la création d'une archive JAR

Il suffit d'inclure directement notre dossier de ressources, sans hiérarchie supplémentaire, (et sans oublier de préciser le point d'entrée) pour avoir une archive JAR complète.
```
~/pacman $ cd bin
~/pacman/bin $ jar -cfe archive.jar com.gforien.PacMan.Jeu com/ resources/
~/pacman/bin $ java -jar archive.jar
```
