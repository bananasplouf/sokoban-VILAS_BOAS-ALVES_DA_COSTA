
import VueControleur.ViewControler;
import modele.Game;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Main
{
    public static void main(String[] args)
    {
        Game game = new Game();
        Observable observer = new Observable();
        Object o = new Object();
        
        ViewControler vc = new ViewControler(game);
        while (true)
        {
            vc.update(observer,o);
        }
    }
}
