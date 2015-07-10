/*
 *  Authors:
 *     Whizzpered,
 *     Yew_Mentzaki.
 */
package org.tmd.environment.entities;

import static java.lang.Math.*;
import org.tmd.environment.particles.Hit;
import org.tmd.render.Animation;
import org.tmd.render.Image;
import org.tmd.render.Sprite;

/**
 *
 * @author Whizzpered
 */
public class Raider extends Entity {

    Animation ghost = new Animation("creatures/ghost");
    int deathtimer = 1000;
    boolean hasMoney = true;

    public Raider(double x, double y, int level) {
        super(x, y);
        this.level = level;
        spriteStanding = new Sprite("creatures/pries");
        minimapIcon = new Image("minimap/warrior.png");
        name = "raider";
        width = 96;
        maxhp = 50;
        deltahp = 15;
        attackDamage = 2;
        attackDeltaDamage = 2;
        faction = 2;
        headType = 0;
        clickable = true;
        level = 1;
    }

    @Override
    public void alive() {
        double dist = Double.MAX_VALUE;
        Entity t = null;
        for (Entity e : dungeon.getEntities()) {
            if (!e.dead && e.faction == 1) {
                double d = sqrt(pow(e.x - x, 2) + pow(e.y - y, 2));
                if (d < dist) {
                    dist = d;
                    t = e;
                }
            }
        }
        if (t != null) {
            goTo(t.x, t.y);
            attack(t);
        }
    }

    @Override
    public void dead() {
        if (hasMoney) {
            hasMoney = false;
            for (int i = 0; i < 4; i++) {
                dungeon.entities.add(new Coin(x, y));
            }
        }
        deathtimer--;
        if (deathtimer == 0) {
            dungeon.entities.remove(this);
            for (int i = 0; i < 4; i++) {
                dungeon.entities.add(new Expa(x, y));
            }
        }
    }

    @Override
    public void render() {
        if (!dead) {
            super.render();
        } else {
            int i = 0;
            if (deathtimer < 100) {
                i = 6 - deathtimer / 14;
                if (i > 6) {
                    i = 6;
                }
                if (i < 0) {
                    i = 0;
                }
            }
            Image g = ghost.images[i];
            g.draw(x - g.width, y - g.height * 2, g.width * 2, g.height * 2);
        }
    }

    @Override
    public void click() {
        dungeon.player.focus = this;
    }
}
