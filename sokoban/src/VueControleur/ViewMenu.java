package VueControleur;

import javax.swing.*;
import java.awt.event.*;

public class ViewMenu extends JFrame
{
    private int windowSizeX;
    private int windowSizeY;
    private boolean next ;
    private boolean level;

    public ViewMenu(int x, int y)
    {
        windowSizeX = x;
        windowSizeY = y;
        next = false;
        level = false;
        setTitle("Menu");
        setSize(windowSizeX,windowSizeY);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre
        setLocationRelativeTo(null);
        setResizable(false);
        JButton play = new JButton("Jouer");
        JButton quit = new JButton("Quitter");
        JButton select = new JButton("Selection Niveaux");
        play.setBounds((windowSizeX/2)-75,(windowSizeY/2)-150,150,50);
        select.setBounds((windowSizeX/2)-75,(windowSizeY/2)-75,150,50);
        quit.setBounds((windowSizeX/2)-75,(windowSizeY/2),150,50);
        add(play);
        add(quit);
        add(select);
        play.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                next = true;
            }
        });
        quit.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                quit();
            }
        });
        select.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                level = true;
                next = true;
            }
        });
        setVisible(true);
    }

    private void quit()
    {
        System.exit(0);
    }

    public boolean getNext()
    {
        return next;
    }

    public boolean getLevel()
    {
        return level;
    }


}
