package VueControleur;

import javax.swing.*;

public class ViewMenu extends JFrame
{
    private int windowSizeX;
    private int windowSizeY;

    public ViewMenu(int x, int y)
    {
        windowSizeX = x;
        windowSizeY = y;
        setTitle("Menu");
        setSize(windowSizeX,windowSizeY);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre
        setLocationRelativeTo(null);
        setResizable(false);
        JButton b = new JButton("Jouer");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setLocation(windowSizeX/2,windowSizeY/2);
        panel.add(b);
        getContentPane().add(panel);
        setVisible(true);
    }

    private void quit()
    {
        System.exit(0);
    }


}
