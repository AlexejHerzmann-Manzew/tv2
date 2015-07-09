/*
 *  Authors:
 *     Whizzpered,
 *     Yew_Mentzaki.
 */
package org.tmd.environment.particles;

import org.tmd.environment.entities.Entity;
import org.tmd.main.Main;
import org.tmd.render.Image;

/**
 *
 * @author yew_mentzaki
 */
public class LevelUp extends Particle{

    Image image = new Image("effects/levelup.png");
    Entity e;
    
    public LevelUp(Entity e) {
        super(e.x, e.y);
        this.e = e;
        timer = 255;
    }

    @Override
    public void renderFloor() {
        x = (float) e.x; y = (float) e.y;
        image.a = (float)timer / 255f;
        image.draw(x, y, image.width * 3 * (1.0-(double)timer/255.0), image.height * 3 * (1.0-(double)timer/255.0), 0);
    }
    
}
