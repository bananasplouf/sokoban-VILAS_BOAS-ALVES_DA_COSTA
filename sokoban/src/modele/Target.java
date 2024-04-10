package modele;

import modele.Game;
import modele.Tile;

public class Target extends Tile {
    private boolean isFilled;

    public Target(Game _game) {
        super(_game);
    }
    @Override
    public boolean canMove()
    {
        return true;
    }

    public boolean isFilled() {
        return e instanceof Block;
    }
}