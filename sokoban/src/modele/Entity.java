package modele;

/**
 * Entités amenées à bouger
 */
public abstract class Entity extends Obj
{

    protected Tile t;

    public Entity(Game _game, Tile _t) { super(_game); t = _t; t.setEntity(this);}

    public void leaveTile() {
        t = null;
    }

    public Tile getTile() {
        return t;
    }

    public void setTile(Tile _t) {
        t = _t;
    }

    public void goOnTile(Tile _t) {
        t = _t;
    }

    public boolean push(Direction d) {
        return false;
    }


    public boolean avancerDirectionChoisie(Direction d)
    {
        return game.moveEntity(this, d);
    }

    @Override
    public abstract String mapString();

}
