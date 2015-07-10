/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmd.environment.entities;

import static java.lang.Math.*;
import org.tmd.environment.particles.BloodParticle;
import org.tmd.environment.particles.Hit;
import org.tmd.environment.particles.LevelUp;
import org.tmd.main.Main;
import org.tmd.render.Image;
import org.tmd.render.gui.Mouse;

/**
 *
 * @author yew_mentzaki
 */
public class Player extends Entity {

    public Image deadSprite = new Image("creatures/player/dead.png");

    public int souls, neededSouls = 12, money;

    public Player(double x, double y) {
        super(x, y);
        minimapIcon = new Image("minimap/player.png");
        name = "Player";
        attackType = "hit_big_clutches";
        faction = 1;
        regenhp = 0.05;
        headType = 1;
        level = 1;
    }

    @Override
    public void alive() {
        attack(focus);
        while (souls >= neededSouls) {
            souls -= neededSouls;
            level++;
            dungeon.addParticle(new LevelUp(this));
            for (Entity e : dungeon.entities) {
                if (e instanceof Mob) {
                    e.level++;
                    dungeon.addParticle(new LevelUp(e));
                }
            }
        }
    }

    @Override
    public void render() {
        if (dead) {
            deadSprite.draw(x, y - deadSprite.height, deadSprite.width * 2, deadSprite.height * 2, 0);
        } else {
            super.render();
        }
    }

    @Override
    public void handle() {
        if (Mouse.left) {
            goTo(Mouse.x - dungeon.cam.x, Mouse.y - dungeon.cam.y);
            //dungeon.pointer.set(Mouse.x - dungeon.cam.x,Mouse.y - dungeon.cam.y);
        }
    }

}
