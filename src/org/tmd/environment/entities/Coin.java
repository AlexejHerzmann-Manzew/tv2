/*
 *  Authors:
 *     Whizzpered,
 *     Yew_Mentzaki.
 */
package org.tmd.environment.entities;

import static java.lang.Math.*;
import org.tmd.main.Main;
import org.tmd.render.Animation;

/**
 *
 * @author yew_mentzaki
 */
public class Coin extends Entity {

    Animation coin = new Animation("effects/coin");

    double vx, vy;

    public Coin(double x, double y) {
        super(x, y);
        double a = Main.RANDOM.nextDouble() * 6.28;
        minimapIcon = null;
        name = "Coin";
        maxhp = Double.MAX_VALUE;
        width = 32;
        vx = cos(a) * 3;
        vy = sin(a) * 3;
        phantom = true;
    }

    @Override
    public void tick() {
        move(vx, vy);
        vx *= 0.99;
        vy *= 0.99;
        Player p = dungeon.player;
        double a = atan2(p.y - 60 - y, p.x - x);
        double d = sqrt(pow(p.x - x, 2) + pow(p.y - y - 60, 2));
        if (d < 15){
            changeStats(p);
            dungeon.entities.remove(this);
        }
        d /= 300;
        if (d < 1) {
            d = (1 - d)/10;
            vx += cos(a) * d;
            vy += sin(a) * d;
        }
    }
    
    public void changeStats(Player p){
         p.money++;
    }

    @Override
    public void render() {
        coin.get().draw(x, y, coin.get().width * 2, coin.get().height * 2, 0);
    }

}
