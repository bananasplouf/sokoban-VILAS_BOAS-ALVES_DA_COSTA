/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

public abstract class Tile extends Obj {

    protected Entity e;
    public abstract boolean canMove();


    // Cette fonction (a redéfinir) détermine le comportement (qui peut être complexe) lorsque l'entité entre dans la case
    public boolean goOnTile(Entity e) {

        //Case c = e.getCase();
        //if (c !=null) {
        //    c.quitterLaCase();
        //}

        setEntity(e);
        return true;
    }

    public void leaveTile() {
        e = null;
    }



    public Tile(Game _game) {
        super(_game);
    }

    public Entity getEntity() {
        return e;
    }

    public void setEntity(Entity _e) {

        e = _e;
        e.setTile(this);}


   }
