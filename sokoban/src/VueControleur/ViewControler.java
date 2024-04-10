package VueControleur;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.tools.Tool;


import modele.*;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 */
public class ViewControler extends JFrame implements Observer
{
    private final Game game; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private final int sizeX; // taille de la grille affichée
    private final int sizeY;

    private boolean isFullscreen;

    private final Dimension screenSize;

    private ViewMenu menuFrame;

    private ViewSelectLevel selectframe;

    int maxWindowSizeX;

    int maxWindowSizeY;

    private int sizeWindowX;

    private int sizeWindowY;

    private final int spriteSize;

    private Timer timer;
    private JLabel timerLabel;
    private int secondsElapsed;






    // icones affichées dans la grille
    private ImageIcon icoHeroMin;
    private ImageIcon icoHeroMax;
    private ImageIcon icoVideMin;
    private ImageIcon icoVideMax;
    private ImageIcon icoMurMin;
    private ImageIcon icoMurMax;
    private ImageIcon icoBlocMin;
    private ImageIcon icoBlocMax;


    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)


    public ViewControler(Game _game)
    {
        sizeX = _game.SIZE_X;
        sizeY = _game.SIZE_Y;
        game = _game;
        sizeWindowX = 800;
        sizeWindowY = 500;
        spriteSize = 64;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        maxWindowSizeX = (int) screenSize.getWidth();
        maxWindowSizeY = (int) screenSize.getHeight(); ;
        isFullscreen = false;
        menuFrame = new ViewMenu(sizeWindowX,sizeWindowY);
        selectframe = new ViewSelectLevel(sizeWindowX,sizeWindowY);
        loadIcons();
        timerLabel = new JLabel("Time: 0 s");
        add(timerLabel);
        pack();
        Color c = new Color(255,0,0);
        timerLabel.setForeground(c);
        timerLabel.setSize(256,256);
        addGraphicComponents();
        addKeyboardListener();

        game.addObserver(this);

        UpdateDisplay();
        switchWindows();
    }

    private void addKeyboardListener()
    {
        addKeyListener(new KeyAdapter()
        { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e)
            {
                switch(e.getKeyCode())
                {  // on regarde quelle touche a été pressée
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

    public void switchWindows()
    {
        while (!menuFrame.getNext())
        {
            menuFrame.setVisible(true);
        }
        while (menuFrame.getNext() && !menuFrame.getLevel())
        {
            menuFrame.dispose();
            setVisible(true);
        }
        while (menuFrame.getLevel())
        {
            menuFrame.dispose();
            selectframe.setVisible(true);
        }
    }

    private void fullscreen()
    {
        if (!isFullscreen)
        {
            setSize(maxWindowSizeX, maxWindowSizeY);
            isFullscreen = true;
            UpdateDisplay();
        }
        else
        {
            setSize(sizeWindowX,sizeWindowY);
            setLocationRelativeTo(null);
            isFullscreen = false;
            UpdateDisplay();
        }
    }

    private void loadIcons()
    {
        icoHeroMin = loadIcons("Images/HeroeMin.png");
        icoHeroMax = loadIcons("Images/HeroeMax.png");
        icoVideMin = loadIcons("Images/EmptyMin.png");
        icoVideMax = loadIcons("Images/EmptyMax.png");
        icoMurMin = loadIcons("Images/wallMin.png");
        icoMurMax = loadIcons("Images/wallMax.png");
        icoBlocMin = loadIcons("Images/ColumnMin.png");
        icoBlocMax = loadIcons("Images/ColumnMax.png");
    }

    private ImageIcon loadIcons(String urlIcone)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new File(urlIcone));
        }
        catch (IOException ex)
        {
            Logger.getLogger(ViewControler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ImageIcon(image);
    }

    private void updateTimerLabel()
    {
        timerLabel.setText("Time: " + secondsElapsed + " s");
    }

    public void startTimer() {
        secondsElapsed = 0;
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    private void addGraphicComponents()
    {
        setTitle("Sokoban");
        setSize(sizeWindowX, sizeWindowY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre
        setLocationRelativeTo(null);

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++)
        {
            for (int x = 0; x < sizeX; x++)
            {
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
    private void UpdateDisplay()
    {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsElapsed++;
                updateTimerLabel();
            }
        });
        timer.start();
        Dimension r = getSize();
        int widthWindow = r.width;
        int heightWindow = r.height;
        float ratioX = (float) widthWindow/maxWindowSizeX;
        float ratioY = (float) heightWindow/maxWindowSizeY;


        for (int x = 0; x < sizeX; x++)
        {
            for (int y = 0; y < sizeY; y++)
            {
                Tile t = game.getGrid()[x][y];

                if (t != null)
                {

                    Entity e = t.getEntity();

                    if (e != null)
                    {
                        if (t.getEntity() instanceof Hero)
                        {
                            if(!isFullscreen)
                            {
                                tabJLabel[x][y].setSize((int)(spriteSize*ratioX),(int)(spriteSize*ratioY));
                                tabJLabel[x][y].setIcon(icoHeroMin);
                            }
                            else
                            {
                                tabJLabel[x][y].setIcon(icoHeroMax);
                            }
                        }
                        else if (t.getEntity() instanceof Block)
                        {
                            if(!isFullscreen)
                            {
                                tabJLabel[x][y].setSize((int)(spriteSize*ratioX),(int)(spriteSize*ratioY));
                                tabJLabel[x][y].setIcon(icoBlocMin);
                            }
                            else
                            {
                                tabJLabel[x][y].setSize((int)(spriteSize*ratioX),(int)(spriteSize*ratioY));
                                tabJLabel[x][y].setIcon(icoBlocMax);
                            }
                        }
                    }
                    else
                    {
                        if (game.getGrid()[x][y] instanceof Wall)
                        {
                            if(!isFullscreen)
                            {
                                tabJLabel[x][y].setSize((int)(spriteSize*ratioX),(int)(spriteSize*ratioY));
                                tabJLabel[x][y].setIcon(icoMurMin);
                            }
                            else
                            {
                                tabJLabel[x][y].setIcon(icoMurMax);
                            }
                        }
                        else if (game.getGrid()[x][y] instanceof Empty)
                        {
                            if(!isFullscreen)
                            {
                                tabJLabel[x][y].setSize((int)(spriteSize*ratioX),(int)(spriteSize*ratioY));
                                tabJLabel[x][y].setIcon(icoVideMin);
                            }
                            else
                            {
                                tabJLabel[x][y].setSize((int)(spriteSize*ratioX),(int)(spriteSize*ratioY));
                                tabJLabel[x][y].setIcon(icoVideMax);
                            }
                        }
                    }



                }

            }
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
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
