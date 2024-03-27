package VueControleur;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;


import modele.*;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 */
public class ViewControler extends JFrame implements Observer {
    private final Game game; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private final int sizeX; // taille de la grille affichée
    private final int sizeY;

    private int sizeWindowX;

     private int sizeWindowY;

    private boolean isFullscreen;

    // icones affichées dans la grille
    private ImageIcon icoHero;
    private ImageIcon icoVide;
    private ImageIcon icoMur;
    private ImageIcon icoBloc;


    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)


    public ViewControler(Game _game) {
        sizeX = _game.SIZE_X;
        sizeY = _game.SIZE_Y;
        game = _game;
        sizeWindowX = 400;
        sizeWindowY = 250;
        isFullscreen = false;

        loadIcons();
        addGraphicComponents();
        addKeyboardListener();

        game.addObserver(this);

        UpdateDisplay();

    }
    private void fullscreen()
    {
        if (!isFullscreen)
        {
            setSize(1920,1080);
            isFullscreen = true;
        }
        else
        {
            setSize(400,250);
            isFullscreen = false;
        }
    }
    private void addKeyboardListener() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée

                    case KeyEvent.VK_LEFT : game.moveHero(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : game.moveHero(Direction.droite); break;
                    case KeyEvent.VK_DOWN : game.moveHero(Direction.bas); break;
                    case KeyEvent.VK_UP : game.moveHero(Direction.haut); break;
                    case KeyEvent.VK_ESCAPE: game.quit(); break;
                    case KeyEvent.VK_F11: fullscreen(); break;


                }
            }
        });
    }


    private void loadIcons() {
        icoHero = loadIcons("Images/Pacman.png");
        icoVide = loadIcons("Images/Vide.png");
        icoMur = loadIcons("Images/Mur.png");
        icoBloc = loadIcons("Images/Colonne.png");
    }

    private ImageIcon loadIcons(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(ViewControler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return new ImageIcon(image);
    }

    private void addGraphicComponents() {
        setTitle("Sokoban");
        setSize(sizeWindowX, sizeWindowY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void UpdateDisplay() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                Tile t = game.getGrid()[x][y];

                if (t != null) {

                    Entity e = t.getEntity();

                    if (e!= null) {
                        if (t.getEntity() instanceof Hero) {
                            tabJLabel[x][y].setIcon(icoHero);
                        } else if (t.getEntity() instanceof Block) {
                            tabJLabel[x][y].setIcon(icoBloc);
                        }
                    } else {
                        if (game.getGrid()[x][y] instanceof Wall) {
                            tabJLabel[x][y].setIcon(icoMur);
                        } else if (game.getGrid()[x][y] instanceof Empty) {

                            tabJLabel[x][y].setIcon(icoVide);
                        }
                    }



                }

            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        UpdateDisplay();
        /*

        // récupérer le processus graphique pour rafraichir
        // (normalement, à l'inverse, a l'appel du modèle depuis le contrôleur, utiliser un autre processus, voir classe Executor)


        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
        */

    }
}
