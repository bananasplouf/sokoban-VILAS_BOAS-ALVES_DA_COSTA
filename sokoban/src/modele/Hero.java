/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 * Héros du jeu
 */
public class Hero extends Entity
{
    public Hero(Game _game, Tile t)
    {
        super(_game, t);
    }

    public boolean isSleeping() {
        return getIndex() != game.getCurrentHero();
    }

    public int getIndex() {
        return game.getHeroIndex(this);
    }
    @Override
    public String mapString() {
        return "H" + getIndex();
    }
}
