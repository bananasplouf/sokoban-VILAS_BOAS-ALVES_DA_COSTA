
import VueControleur.ViewControler;
import modele.Game;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        
        ViewControler vc = new ViewControler(game);
        vc.setVisible(true);

    }
}
