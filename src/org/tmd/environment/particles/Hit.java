/*
 *  Authors:
 *     Whizzpered,
 *     Yew_Mentzaki.
 */
package org.tmd.environment.particles;

import org.tmd.main.Main;
import org.tmd.render.Image;

/**
 *
 * @author yew_mentzaki
 */
public class Hit extends Particle {

    Image images[] = new Image[6];
    boolean mirror = Main.RANDOM.nextBoolean();

    public Hit(String type, double x, double y) {
        super(x + (Main.RANDOM.nextInt(31) - 15), y + (Main.RANDOM.nextInt(31) - 15));
        for (int i = 0; i < 6; i++) {
            images[i] = new Image("effects/" + type + "/" + i + ".png");
        }
        timer = 90;
    }

    @Override
    public void renderEntity() {
        int i = (90 - timer) / 15;
        if (i > 5) {
            i = 5;
        }
        images[i].draw(x, y, images[i].width * 2 * (mirror?1:-1), images[i].height * 2, 0);
    }

}
