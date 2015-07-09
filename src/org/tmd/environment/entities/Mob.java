/*
 *  Authors:
 *     Whizzpered,
 *     Yew_Mentzaki.
 */
package org.tmd.environment.entities;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import org.tmd.environment.Point;
import org.tmd.environment.particles.Hit;
import org.tmd.environment.particles.LevelUp;
import org.tmd.main.Main;
import org.tmd.render.Animation;
import org.tmd.render.Image;
import org.tmd.render.Sprite;

/**
 *
 * @author Whizzpered
 */
public class Mob extends Entity {

    public double range;
    public Point spawn = new Point();
    public static Animation come = new Animation("creatures/mob/death");
    public int comeTimer = 150;

    public Mob(double x, double y) {
        super(x, y);
        spawn.x = (int) x;
        spawn.y = (int) y;
        spriteStanding = new Sprite("creatures/mob");
        minimapIcon = new Image("minimap/monkey.png");
        attackType = "hit_clutches";
        range = 400;
        name = "monkey";
        maxhp = 30;
        deltahp = 10;
        attackDamage = 3;
        attackDeltaDamage = 2;
        faction = 1;
    }

    @Override
    public void tick() {
        super.tick();
        if (dead) {
            if (comeTimer < 150) {
                comeTimer++;
                if (comeTimer == 150) {
                    x = spawn.x;
                    y = spawn.y;
                }
            }
        } else {
            if (comeTimer > 0) {
                comeTimer--;
            }
        }
        patrool();
    }

    @Override
    public void dead() {
        int lvl = 0;
        for (Entity e : dungeon.getEntities()) {
            if (e instanceof Raider && !e.dead) {
                lvl = e.level;
                return;
            }
        }
        dead = false;
        hp = getMaxHP();
        if (lvl > level && Main.RANDOM.nextInt(3) == 0) {
            level = lvl;
            dungeon.addParticle(new LevelUp(this));
        }
    }

    @Override
    public void render() {
        if (comeTimer <= 0 || comeTimer >= 150) {
            if (!dead) {
                super.render();
            }
        } else {
            come.images[comeTimer / 15].draw(x - come.images[comeTimer / 15].width, y - come.images[comeTimer / 15].height * 2, come.images[comeTimer / 15].width * 2, come.images[comeTimer / 15].height * 2);
        }
    }

    @Override
    public void attack(Entity e) {
        if (attackReload == 0) {
            if (sqrt(pow(e.x - x, 2) + pow(e.y - y, 2)) < attackDistance) {
                dungeon.addParticle(new Hit(attackType, e.x, e.y - 35));
                boolean killed = e.hit(attackDamage, this);
                if (killed) {
                    level++;
                    dungeon.addParticle(new LevelUp(this));
                }
                attackReload = attackReloadTime;
            }
        }
    }

    public void patrool() {
        if (focus == null) {
            for (Entity e : dungeon.getEntities()) {
                if (e instanceof Raider && !e.dead) {
                    double dist = Math.sqrt(Math.pow(x - e.x, 2) + Math.pow(y - e.y, 2));
                    if (dist <= range) {
                        focus = e;
                        return;
                    }
                }
            }
        } else {
            if (!focus.dead) {
                attack(focus);
                goTo(focus.x, focus.y);
            } else {
                focus = null;
                goTo(spawn.x, spawn.y);
            }
        }
    }
}
