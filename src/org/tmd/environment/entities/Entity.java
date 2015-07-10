/*
 *  Authors:
 *     Whizzpered,
 *     Yew_Mentzaki.
 */
package org.tmd.environment.entities;

import static java.lang.Math.*;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.tmd.environment.Block;
import org.tmd.environment.Point;
import org.tmd.environment.particles.BloodParticle;
import org.tmd.environment.particles.HeadParticle;
import org.tmd.environment.particles.Hit;
import org.tmd.main.Declaration;
import org.tmd.main.GameLocale;
import org.tmd.main.Main;
import org.tmd.render.Image;
import org.tmd.render.Side;
import org.tmd.render.Sprite;
import org.tmd.render.gui.Mouse;
import org.tmd.render.scenes.Dungeon;

/**
 *
 * @author yew_mentzaki
 */
public class Entity {

    public String name = "Entity";
    public Dungeon dungeon = Declaration.dungeon;
    public Entity focus;
    public double x, y, size = 75, width = 128, height = 48, hp = 105, maxhp = 100, deltahp = 25, regenhp = 0.01, armor;
    protected double targetX = -1, targetY = -1;
    private Point[] way;
    private int currentWaypoint;
    public Sprite spriteStanding = new Sprite("creatures/player");
    public Image minimapIcon = new Image("minimap/entity.png");
    public Side side = Side.FRONT;
    public double speed = 2;
    public int faction;
    public boolean phantom = false, dead;
    public int attackDamage = 10, attackDeltaDamage = 5;
    public int attackDistance = 128;
    public int headType = -1;
    public String attackType = "hit_sword";
    public int attackReloadTime = 100;
    protected int attackReload = 0;
    public int level = 0;
    public boolean clickable;

    public String getName() {
        return GameLocale.get(name);
    }
    
    public void click(){
        
    }

    public void attack(Entity e) {
        if (attackReload == 0 && e != null) {
            if (sqrt(pow(e.x - x, 2) + pow(e.y - y, 2)) < attackDistance) {
                dungeon.addParticle(new Hit(attackType, e.x, e.y - 35));
                e.hit(getDMG(), this);
                attackReload = attackReloadTime;
            }
        }
    }

    public double getHP() {
        return hp;
    }

    public double getMaxHP() {
        return maxhp + deltahp * level;
    }

    public double getDMG() {
        return attackDamage + attackDeltaDamage * level;
    }

    public double getArmor() {
        return armor;
    }

    private int sign(double a) {
        if (a > 0) {
            return 1;
        }
        if (a < 0) {
            return -1;
        }
        return 0;
    }

    public Entity(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void alive() {

    }

    public void dead() {

    }

    public boolean shearable(Point start, Point end) {
        int d = (int) Math.sqrt(Math.pow(start.x - end.x, 2) + Math.pow(start.y - end.y, 2)) / 4,
                s = (int) (Math.sqrt(Math.pow(start.x - end.x, 2) + Math.pow(start.y - end.y, 2)) / d);
        double angle = Math.atan2(end.y - start.y, end.x - start.x);

        for (int i = 1; i <= s; i++) {
            double l = (i * d * Math.cos(angle)),
                    k = (i * d * Math.sin(angle));
            if (dungeon.terrain.get(start.x + l - width / 2, start.y + k).solid
                    || dungeon.terrain.get(start.x + l + width / 2, start.y + k).solid
                    || dungeon.terrain.get(start.x + l / 2, start.y + k - height).solid
                    || dungeon.terrain.get(start.x + l / 2, start.y + k + height).solid) {

                return false;
            }
        }

        return true;
    }

    public void goTo(double goToX, double goToY) {
        int tx = (int) (goToX / Block.BLOCK_WIDTH), ty = (int) (goToY / Block.BLOCK_HEIGHT);
        final int fx = (int) (this.x / Block.BLOCK_WIDTH), fy = (int) (this.y / Block.BLOCK_HEIGHT);
        boolean player = getClass() == Player.class;
        if (player && dungeon.terrain.get(tx, ty).enemyZone) {
            return;
        }
        if (way != null) {
            int tx2 = (int) (way[way.length - 1].x / Block.BLOCK_WIDTH), ty2 = (int) (way[way.length - 1].y / Block.BLOCK_HEIGHT);
            if (tx == tx2 && ty == ty2) {
                way[way.length - 1].x = goToX;
                way[way.length - 1].y = goToY;
                return;
            }
        }
        final boolean[][] dung = new boolean[dungeon.terrain.width][dungeon.terrain.height];
        if (tx < 0 || ty < 0 || tx >= dungeon.terrain.width || ty >= dungeon.terrain.height) {
            return;
        }
        for (int x = 0; x < dungeon.terrain.width; x++) {
            for (int y = 0; y < dungeon.terrain.height; y++) {
                if (!player) {
                    dung[x][y] = !(dungeon.terrain.get(x, y).solid);
                } else {
                    dung[x][y] = !(dungeon.terrain.get(x, y).solid | dungeon.terrain.get(x, y).enemyZone);
                }
            }
        }
        class Waypoint {

            int x, y;
            Waypoint parent;

            public Waypoint(int x, int y, Waypoint parent) {
                this.x = x;
                this.y = y;
                this.parent = parent;
                dung[this.x][this.y] = false;
            }

            public Waypoint(int x, int y) {
                this.x = x;
                this.y = y;
                dung[this.x][this.y] = false;
            }

            public boolean growTo(int x, int y, ArrayList<Waypoint> w) {
                if (this.x + x >= 0
                        && this.y + y >= 0
                        && this.x + x < dungeon.terrain.width
                        && this.y + y < dungeon.terrain.height) {
                    if (x == 0 || y == 0) {
                        if (dung[this.x + x][this.y + y]) {
                            w.add(new Waypoint(this.x + x, this.y + y, this));
                        }
                    } else {
                        if (dung[this.x + x][this.y] & dung[this.x + x][this.y + y] & dung[this.x][this.y + y]) {
                            w.add(new Waypoint(this.x + x, this.y + y, this));
                        }
                    }
                }
                return ((this.x + x == fx) && (this.y + y == fy));
            }

            public Waypoint grow(ArrayList<Waypoint> w) {
                if (growTo(1, 1, w)
                        || growTo(-1, 1, w)
                        || growTo(-1, -1, w)
                        || growTo(1, -1, w)
                        || growTo(1, 0, w)
                        || growTo(-1, 0, w)
                        || growTo(0, 1, w)
                        || growTo(0, -1, w)) {
                    return new Waypoint(fx, fy, this);
                } else {
                    return null;
                }
            }
        }
        ArrayList<Waypoint> wp = new ArrayList<Waypoint>();
        wp.add(new Waypoint(tx, ty));
        int i = 0;
        do {
            ArrayList<Waypoint> w = new ArrayList<Waypoint>();
            for (Waypoint v : wp) {
                Waypoint cwp = v.grow(w);
                if (cwp != null) {
                    wp = new ArrayList<Waypoint>();
                    while (cwp.parent != null) {
                        wp.add(cwp);
                        cwp = cwp.parent;
                    }
                    wp.add(cwp);
                    Point[] p = new Point[wp.size()];
                    for (int j = 0; j < p.length; j++) {
                        p[j] = new Point(wp.get(j).x * Block.BLOCK_WIDTH + Block.BLOCK_WIDTH / 2, wp.get(j).y * Block.BLOCK_HEIGHT + Block.BLOCK_HEIGHT / 2);
                    }
                    p[p.length - 1].x = goToX;
                    p[p.length - 1].y = goToY;
                    this.way = p;
                    this.currentWaypoint = 0;
                    return;
                }
            }
            wp = w;
            i = w.size();
        } while (i != 0);
    }

    public void tick() {
        if (hp > 0) {
            if (attackReload > 0) {
                attackReload--;
            }
            Point[] way = this.way;
            if (way != null && currentWaypoint < way.length) {
                walk(cos(atan2(way[currentWaypoint].y - y, way[currentWaypoint].x - x)) * speed, sin(atan2(way[currentWaypoint].y - y, way[currentWaypoint].x - x)) * speed);
                if (sqrt(pow(x - way[currentWaypoint].x, 2) + pow(y - way[currentWaypoint].y, 2)) <= Block.BLOCK_WIDTH / 2) {
                    currentWaypoint++;
                    if (currentWaypoint == way.length) {
                        this.way = null;
                        currentWaypoint = 0;
                    } else {
                        while (currentWaypoint + 1 < way.length) {
                            if (shearable(new Point(x, y), new Point(way[currentWaypoint + 1].x, way[currentWaypoint + 1].y))) {
                                currentWaypoint++;
                            } else {
                                break;
                            }
                        }
                    }
                }
            } else if (targetX != -1) {
                walk(cos(atan2(targetY - y, targetX - x)) * speed, sin(atan2(targetY - y, targetX - x)) * speed);
                if (sqrt(pow(x - targetX, 2) + pow(y - targetY, 2)) <= speed * 2) {
                    targetX = -1;
                    targetY = -1;
                }
            }
            if (!phantom) {
                for (Entity e : dungeon.getEntities()) {
                    if (e == null || e.phantom) {
                        continue;
                    }
                    double d = sqrt(pow(e.x - x, 2) + pow(e.y - y, 2));
                    if (d < (e.size + size) / 2) {
                        double a = atan2(e.y - y, e.x - x);
                        e.move(cos(a) * e.speed / 2, sin(a) * e.speed / 2);
                        move(-cos(a) * speed / 2, -sin(a) * speed / 2);
                    }
                }
            }
            hp += regenhp;
            if (hp > getMaxHP()) {
                hp = getMaxHP();
            }
            alive();
        } else {
            dead = true;
            dead();
        }
    }

    public void handle() {

    }

    public void move(double x, double y) {
        if (!dungeon.terrain.get(this.x + x + sign(x) * width / 2, this.y + y + sign(y) * height / 2).solid) {
            this.x += x;
            this.y += y;
        } else if (!dungeon.terrain.get(this.x + x + sign(x) * size / 2, this.y).solid) {
            this.x += x;
        } else if (!dungeon.terrain.get(this.x, this.y + y + sign(y) * height / 2).solid) {
            this.y += y;
        }

    }

    public void walk(double x, double y) {
        if (x != 0 && y != 0) {
            if (Math.abs(x) > Math.abs(y)) {
                if (x < 0) {
                    side = side.LEFT;
                } else {
                    side = side.RIGHT;
                }
            } else {
                if (y < 0) {
                    side = side.BACK;
                } else {
                    side = side.FRONT;
                }
            }
        }
        move(x, y);
    }

    public void longTick() {

    }

    public void render() {
        spriteStanding.render(side, x, y);
    }

    public int getRenderQueuePriority() {
        return (int) y;
    }

    public boolean hit(double damage, Entity from) {
        if (hp <= 0) {
            return false;
        }
        hp -= damage;
        if (Main.RANDOM.nextBoolean()) {
            dungeon.addParticle(new BloodParticle(x, y - 35));
        }
        if (hp < 0) {
            if (headType >= 0 && Main.RANDOM.nextBoolean()) {
                dungeon.addParticle(new HeadParticle(headType, x, y - 75));
            }
            for (int i = 0; i < Main.RANDOM.nextInt(4); i++) {
                dungeon.addParticle(new BloodParticle(x, y - 35));
            }
        }
        return hp < 0;
    }
}
