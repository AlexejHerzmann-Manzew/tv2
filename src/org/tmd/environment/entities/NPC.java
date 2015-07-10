/*
 *  Authors:
 *     Whizzpered,
 *     Yew_Mentzaki.
 */
package org.tmd.environment.entities;

import org.tmd.render.Animation;
import org.tmd.render.Image;

/**
 *
 * @author yew_mentzaki
 */
public class NPC extends Entity{

    Animation sprite;
    String dialog;
    
    public NPC(double x, double y, String sprite, String name, String dialog) {
        super(x, y);
        this.sprite = new Animation(sprite);
        this.name = name;
        this.sprite.delay = 250;
        this.dialog = dialog;
        maxhp = Double.MAX_VALUE;
        phantom = true;
    }

    @Override
    public void tick() {
        
    }

    @Override
    public void render() {
        sprite.get().draw(x - sprite.get().width, y - sprite.get().height * 2, sprite.get().width * 2, sprite.get().height * 2);
    }
    
}
