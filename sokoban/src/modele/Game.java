/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;


import javax.swing.*;
import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;


public class Game extends Observable
{

    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;


    private Hero hero;

    private final HashMap<Tile, Point> map = new  HashMap<Tile, Point>(); // permet de récupérer la position d'une case à partir de sa référence
    private final Tile[][] entityGrid = new Tile[SIZE_X][SIZE_Y]; // permet de récupérer une case à partir de ses coordonnées



    public Game()
    {
        initializeLevel();
    }


    
    public Tile[][] getGrid() {
        return entityGrid;
    }
    
    public Hero getHero() {
        return hero;
    }

    public void moveHero(Direction d)
    {
        hero.avancerDirectionChoisie(d);
        setChanged();
        notifyObservers();
    }


    public void quit()
    {
        System.exit(0);
    }
    
    private void initializeLevel()
    {

        // murs extérieurs horizontaux
        for (int x = 0; x < SIZE_X; x++)
        {
            addTile(new Wall(this), x, 0);
            addTile(new Wall(this), x, SIZE_Y-1);
        }

        // murs extérieurs verticaux
        for (int y = 1; y < SIZE_Y-1; y++)
        {
            addTile(new Wall(this), 0, y);
            addTile(new Wall(this), SIZE_X-1, y);
        }

        for (int x = 1; x < SIZE_X-1; x++)
        {
            for (int y = 1; y < 9; y++)
            {
                addTile(new Empty(this), x, y);
            }
        }

        hero = new Hero(this, entityGrid[4][4]);
        Block b = new Block(this, entityGrid[6][6]);
    }

    private void addTile(Tile e, int x, int y)
    {
        entityGrid[x][y] = e;
        map.put(e, new Point(x, y));
    }


    
    /** Si le déplacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     * Sinon, rien n'est fait.
     */
    public boolean moveEntity(Entity e, Direction d)
    {
        boolean back = true;
        
        Point pointPosition = map.get(e.getTile());
        
        Point pointTarget = calculateTargetPoint(pointPosition, d);

        if (isInGrid(pointTarget))
        {
            Entity entityTarget= tilePosition(pointTarget).getEntity();
            if (entityTarget != null)
            {
                entityTarget.push(d);
            }

            // si la case est libérée
            if (tilePosition(pointTarget).canMove())
            {
                e.getTile().leaveTile();
                tilePosition(pointTarget).goOnTile(e);

            }
            else
            {
                back = false;
            }

        }
        else
        {
            back = false;
        }

        return back;
    }
    
    
    private Point calculateTargetPoint(Point pointPosition, Direction d)
    {
        Point pointTarget = null;
        
        switch(d)
        {
            case haut: pointTarget = new Point(pointPosition.x, pointPosition.y - 1); break;
            case bas : pointTarget = new Point(pointPosition.x, pointPosition.y + 1); break;
            case gauche : pointTarget = new Point(pointPosition.x - 1, pointPosition.y); break;
            case droite : pointTarget = new Point(pointPosition.x + 1, pointPosition.y); break;
            
        }
        
        return pointTarget;
    }
    

    
    /** Indique si p est contenu dans la grille
     */
    private boolean isInGrid(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    private Tile tilePosition(Point p)
    {
        Tile back = null;
        
        if (isInGrid(p))
        {
            back = entityGrid[p.x][p.y];
        }
        
        return back;
    }

}
