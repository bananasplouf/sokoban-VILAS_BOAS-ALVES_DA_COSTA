package modele;

public class Empty extends Tile
{

    public Empty(Game _game) { super(_game); }

    @Override
    public boolean canMove()
    {
        return e == null;
    }
    @Override
    public String mapString() {
        return "_";
    }
}
