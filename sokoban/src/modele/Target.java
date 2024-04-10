package modele;

import modele.Game;
import modele.Tile;

public class Goal extends Tile {
    public Goal(Game _game) {
        super(_game);
    }

    @Override
    public boolean canMove() {
        return false;
    }
}