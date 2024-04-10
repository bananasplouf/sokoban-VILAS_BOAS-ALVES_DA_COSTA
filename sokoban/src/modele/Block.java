package modele;

public class Block extends Entity
{


    public Block(Game _game, Tile t)
    {
        super(_game, t);
    }

    public boolean push(Direction d)
    {
        return game.moveEntity(this, d);
    }
    public int getIndex() {
        return game.getBlockIndex(this);
    }
    @Override
    public String mapString() {
        return "B" + getIndex();
    }

}
