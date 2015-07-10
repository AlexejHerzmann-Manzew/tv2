/*
 *  Authors:
 *     Whizzpered,
 *     Yew_Mentzaki.
 */
package org.tmd.environment.particles;

import org.tmd.main.Declaration;
import org.tmd.main.Main;
import org.tmd.render.Image;

/**
 *
 * @author yew_mentzaki
 */
public class HeadParticle extends Particle {

    float vx, vy, a;
    static Image head = new Image("effects/raider_head.png");
    static Image phead = new Image("effects/player_head.png");
    static Image mhead = new Image("effects/mob_head.png");
    int type;

    public HeadParticle(int t, double x, double y) {
        super(x + (Main.RANDOM.nextInt(21) - 10), y + (Main.RANDOM.nextInt(21) - 15));
        type = t;
        vx = (Main.RANDOM.nextInt(11) - 5) / 3f;
        vy = (Main.RANDOM.nextInt(5) - 5) / 3f;
        a = vx / 8;
    }

    @Override
    public void tick() {
        super.tick();
        x += vx;
        y += vy;
        vy += 0.02f;
        a += vx / 16;
        if (vy > Main.RANDOM.nextInt(5) + 2) {
            vy = -vy / 2;
        }
        if (timer == 0) {
            Declaration.dungeon.addParticle(new BloodPool(x, y));
        }
    }

    @Override
    public void renderEntity() {
        if (type == 0) {
            head.draw(x, y, head.width * 2, head.height * 2, a);
        }
        if (type == 1) {
            phead.draw(x, y, phead.width * 2, phead.height * 2, a);
        }
        if (type == 2) {
            mhead.draw(x, y, mhead.width * 2, mhead.height * 2, a);
        }
    }

}
