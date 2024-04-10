package VueControleur;
import javax.swing.*;
import java.awt.event.*;

public class ViewSelectLevel extends JFrame
{
    private int windowSizeX;
    private int windowSizeY;
    private boolean next ;
    public ViewSelectLevel(int x, int y)
    {
        windowSizeX = x;
        windowSizeY = y;
        next = false;
        setTitle("Selection Niveau");
        setSize(windowSizeX,windowSizeY);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre
        setLocationRelativeTo(null);
        setResizable(false);
        JButton level0 = new JButton("Niveau 0");
        JButton level1 = new JButton("Niveau 1");
        JButton level2 = new JButton("Niveau 2");
        JButton level3 = new JButton("Niveau 3");
        JButton level4 = new JButton("Niveau 4");
        JButton quit = new JButton("Quitter");
        level0.setBounds((windowSizeX/2)-75,(windowSizeY/2)-250,150,50);
        level1.setBounds((windowSizeX/2)-75,(windowSizeY/2)-200,150,50);
        level2.setBounds((windowSizeX/2)-75,(windowSizeY/2)-150,150,50);
        level3.setBounds((windowSizeX/2)-75,(windowSizeY/2)-100,150,50);
        level4.setBounds((windowSizeX/2)-75,(windowSizeY/2)-50,150,50);
        quit.setBounds((windowSizeX/2)-75,(windowSizeY/2),150,50);
        add(level0);
        add(level1);
        add(level2);
        add(level3);
        add(level4);
        add(quit);
        level0.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                next = true;
            }
        });
        level1.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                next = true;
            }
        });
        level2.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                next = true;
            }
        });
        level3.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                next = true;
            }
        });
        level4.addMouseListener(new MouseAdapter()
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
    }
    private void quit()
    {
        System.exit(0);
    }

    public boolean getNext()
    {
        return next;
    }

    public void setNext(boolean b)
    {
        next = b;
    }
}
