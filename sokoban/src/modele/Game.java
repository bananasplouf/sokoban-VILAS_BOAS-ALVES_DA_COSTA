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
import java.util.List; // Pour gérer une liste
import java.util.ArrayList; // Pour gérer une liste
import java.io.PrintWriter; // Pour écrire dans un fichier
import java.io.IOException; // Pour gérer les exceptions liées à l'écriture dans un fichier
import java.io.BufferedReader; // Pour lire un fichier
import java.io.FileReader; // Pour lire un fichier
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Game extends Observable
{

    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;


    private final List<Hero> heroes = new ArrayList<>();

    private final HashMap<Tile, Point> map = new  HashMap<Tile, Point>(); // permet de récupérer la position d'une case à partir de sa référence
    private final Tile[][] entityGrid = new Tile[SIZE_X][SIZE_Y]; // permet de récupérer une case à partir de ses coordonnées
    private final List<Target> targets = new ArrayList<>();
    private final List<Block> blocks = new ArrayList<>();
    private int currentHero = 0;


    public Game()
    {
        readMapFromFile("maps/level1");
    }


    
    public Tile[][] getGrid() {
        return entityGrid;
    }
    
    public Hero getHero(int i) {
        return heroes.get(i);
    }

    public void moveHero(Direction d,int i) {
        heroes.get(i).avancerDirectionChoisie(d);
        setChanged();
        notifyObservers();
    }
    public void moveHero(Direction d) {
        moveHero(d,currentHero);
    }

    public int getBlockIndex(Block b) {
        return blocks.indexOf(b);
    }
    public int getTargetIndex(Target t) {
        return targets.indexOf(t);
    }

    public int getHeroIndex(Hero h) {
        return heroes.indexOf(h);
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

        heroes.add(new Hero(this, entityGrid[4][4]));
        blocks.add(new Block(this, entityGrid[6][6]));

        targets.add(new Target(this));
        addTile(targets.get(0), 17, 7);
    }

    private void addTile(Tile e, int x, int y) {
        entityGrid[x][y] = e;
        map.put(e, new Point(x, y));
    }


    /**
     * Si le déplacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     * Sinon, rien n'est fait.
     */
    public boolean moveEntity(Entity e, Direction d) {
        boolean back = true;

        Point pointPosition = map.get(e.getTile());

        Point pointTarget = calculateTargetPoint(pointPosition, d);

        if (isInGrid(pointTarget)) {
            Entity entityTarget = tilePosition(pointTarget).getEntity();
            if (entityTarget != null) {
                entityTarget.push(d);
            }

            // si la case est libérée
            if (tilePosition(pointTarget).canMove()) {
                e.getTile().leaveTile();
                tilePosition(pointTarget).goOnTile(e);

            } else {
                back = false;
            }

        } else {
            back = false;
        }

        return back;
    }


    private Point calculateTargetPoint(Point pointPosition, Direction d) {
        Point pointTarget = null;

        switch (d) {
            case haut:
                pointTarget = new Point(pointPosition.x, pointPosition.y - 1);
                break;
            case bas:
                pointTarget = new Point(pointPosition.x, pointPosition.y + 1);
                break;
            case gauche:
                pointTarget = new Point(pointPosition.x - 1, pointPosition.y);
                break;
            case droite:
                pointTarget = new Point(pointPosition.x + 1, pointPosition.y);
                break;

        }

        return pointTarget;
    }


    /**
     * Indique si p est contenu dans la grille
     */
    private boolean isInGrid(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }

    private Tile tilePosition(Point p) {
        Tile back = null;

        if (isInGrid(p)) {
            back = entityGrid[p.x][p.y];
        }

        return back;
    }



    public void writeMapToFile(String fileName) {
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.println(SIZE_X);
            writer.println(SIZE_Y);
            for (int i = 0; i < SIZE_Y; i++) {
                for (int j = 0; j < SIZE_X; j++) {
                    Tile t = entityGrid[j][i];
                    String type = "";
                    if (t.getEntity() instanceof Block) {
                        type = "B" + blocks.indexOf(t.getEntity());
                    } else if (t instanceof Target) {
                        type = "T" + targets.indexOf(t);
                    } else if (t.getEntity() instanceof Hero) {
                        type = "H"+ heroes.indexOf(t.getEntity());
                    } else if (t instanceof Empty) {
                        type = "_";
                    } else if (t instanceof Wall) {
                        type = "W";
                    }
                    writer.print( type + ",");
                }
                writer.println();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    /**
     * Reads a map from a file and updates the entityGrid and hero accordingly
     *
     * @param fileName the name of the file to read from
     */
    public void readMapFromFile(String fileName) {
        Hero[] tempHeroes = new Hero[ 256 ];
        Block[] tempBlocks = new Block[ 256 ];
        Target[] tempTargets = new Target[ 256 ];
        for (int x = 1; x < SIZE_X-1; x++)
        {
            for (int y = 1; y < 9; y++)
            {
                addTile(new Empty(this), x, y);
            }
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            int x = Integer.parseInt(reader.readLine());
            int y = Integer.parseInt(reader.readLine());
            Tile[][] entityGrid = new Tile[x][y];
            for (int i = 0; i < y; i++) {
                String line = reader.readLine();
                String[] tiles = line.split(",");
                for (int j = 0; j < x; j++) {
                    String type = tiles[j];
                    Pattern pattern = Pattern.compile("([HBT])(\\d+)");
                    Matcher matcher = pattern.matcher(type);
                    if (matcher.find()) {
                        String letter = matcher.group(1);
                        int num = Integer.parseInt(matcher.group(2));
                        System.out.println("lettre num : " + letter + " " + num);
                        switch (letter) {

                            case "B":
                                addTile(new Empty(this), j, i);
                                tempBlocks[num] = new Block(this, entityGrid[j][i]);
                                entityGrid[j][i].setEntity(tempBlocks[num]);
                                break;
                            case "H":
                                System.out.println("hero : " + tempHeroes[num]);
                                addTile(new Empty(this), j, i);
                                tempHeroes[num] = new Hero(this, entityGrid[j][i]);

                                entityGrid[j][i].setEntity(tempHeroes[num]);
                                break;
                            case "T":
                                tempTargets[num] = new Target(this);
                                addTile(tempTargets[num], j, i);
                                break;
                        }
                    } else {
                        switch (tiles[j]) {
                            case "_":
                                addTile(new Empty(this), j, i);
                                break;
                            case "W":
                                addTile(new Wall(this), j, i);
                                break;
                        }
                    }
                }
            }
            reader.close();
            heroes.clear();
            blocks.clear();
            targets.clear();
            for (Hero hero : tempHeroes) {
                if (hero != null) {
                    heroes.add(hero);
                }
            }
            for (Block block : tempBlocks) {
                if (block != null) {
                    blocks.add(block);
                }
            }
            for (Target target : tempTargets) {
                if (target != null) {
                    targets.add(target);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading from the file.");
            e.printStackTrace();
        }
    }

    public void checkGameOver() {
        for (int i = 0; i < targets.size(); i++) {
            if ( !targets.get(i).isFilled() || blocks.indexOf( targets.get(i).getEntity() ) == i){
                return;
            }
        }
        // Si on arrive ici, toutes les cibles sont remplies
        System.out.println("Game Over! You won!");
        // Ici, vous pouvez ajouter du code pour terminer le jeu, par exemple en affichant un message de victoire
    }
}