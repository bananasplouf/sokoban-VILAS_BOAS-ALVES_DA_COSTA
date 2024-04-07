package modele;

/**
 * Niveau le plus abstrait des objets manipulés dans le modèle (attention, il ne s'agit pas de la classe Objet ...))
 */
public abstract class Obj
{
    protected Game game;
    public Obj(Game _game)
    {
        game = _game;
    }
}
