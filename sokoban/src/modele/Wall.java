package modele;

public class Wall extends Tile
{
    public Wall(Game _game) { super(_game);}

    @Override
    public boolean canMove()
    {
        return false;
    }
    @Override
    public String mapString()
    {
        return "W";
    }
}
