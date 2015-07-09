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

    public int souls, neededSouls = 12, money;
    public Player(double x, double y) {
        super(x, y);
        minimapIcon = new Image("minimap/player.png");
        name = "Player";
        attackType = "hit_big_clutches";
        faction = 1;
        regenhp = 0.05;
    }

    @Override
    public void alive() {
        while (souls >= neededSouls) {
            souls -= neededSouls;
            level++;
            dungeon.addParticle(new LevelUp(this));
        }
    }

    @Override
    public void handle() {
        if (Mouse.left) {
            goTo(Mouse.x - dungeon.cam.x, Mouse.y - dungeon.cam.y);
            //dungeon.pointer.set(Mouse.x - dungeon.cam.x,Mouse.y - dungeon.cam.y);
        }
    }

    @Override
    public boolean hit(double damage, Entity from) {
        attack(from);
        return super.hit(damage, from); //To change body of generated methods, choose Tools | Templates.
    }

}
