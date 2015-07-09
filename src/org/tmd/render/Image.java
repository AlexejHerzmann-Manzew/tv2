 /*
 *  Authors:
 *     Whizzpered,
 *     Yew_Mentzaki.
 */
package org.tmd.render;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author yew_mentzaki
 */
public class Image {

    public final String path;
    private transient org.newdawn.slick.Image subimage;
    public int width, height;
    public float r = 1, g = 1, b = 1, a = 1;

    public Image(String path) {
        this.path = path;
        this.subimage = Textures.image(path);
        this.width = subimage.getWidth();
        this.height = subimage.getHeight();
    }

    Image(Textures.Tex tex) {
        this.path = tex.name;
        this.subimage = tex.image;
        this.width = 1;
        this.height = 1;
    }

    public Image getNxCopy(float n) {
        subimage = subimage.getScaledCopy(n);
        subimage.setFilter(GL11.GL_NEAREST);
        return this;
    }

    public Image getFlipped(boolean horizontal, boolean vertical) {
        subimage = subimage.getFlippedCopy(horizontal, vertical);
        subimage.setFilter(GL11.GL_NEAREST);
        return this;
    }

    public void draw() {
        if (subimage == null) {
            this.subimage = Textures.image(path);
            this.width = subimage.getWidth();
            this.height = subimage.getHeight();
        }
        subimage.setImageColor(r, g, b, a);
        subimage.draw(0, 0);
    }

    public void draw(double x, double y) {
        if (subimage == null) {
            this.subimage = Textures.image(path);
            this.width = subimage.getWidth();
            this.height = subimage.getHeight();
        }
        subimage.setImageColor(r, g, b, a);
        subimage.draw((float) x, (float) y);
    }

    public void draw(double x, double y, double width, double height) {
        if (subimage == null) {
            this.subimage = Textures.image(path);
            this.width = subimage.getWidth();
            this.height = subimage.getHeight();
        }
        subimage.setImageColor(r, g, b, a);
        subimage.draw((float) x, (float) y, (float) width, (float) height);
    }

    public void draw(double angle) {
        if (subimage == null) {
            this.subimage = Textures.image(path);
            this.width = subimage.getWidth();
            this.height = subimage.getHeight();
        }
        subimage.setImageColor(r, g, b, a);
        draw(0, 0, angle);
    }

    public void draw(double x, double y, double angle) {
        if (subimage == null) {
            this.subimage = Textures.image(path);
            this.width = subimage.getWidth();
            this.height = subimage.getHeight();
        }
        subimage.setImageColor(r, g, b, a);
        draw(x, y, width, height, angle);
    }

    public void draw(double x, double y, double width, double height, double angle) {
        if (subimage == null) {
            this.subimage = Textures.image(path);
            this.width = subimage.getWidth();
            this.height = subimage.getHeight();
        }
        subimage.setImageColor(r, g, b, a);
        GL11.glTranslated(x, y, 0);
        GL11.glRotated(angle / Math.PI * 180.0, 0, 0, 1);
        subimage.draw((float) -width / 2f, (float) -height / 2f, (float) width, (float) height);
        GL11.glRotated(angle / Math.PI * 180.0, 0, 0, -1);
        GL11.glTranslated(-x, -y, 0);
    }

    public void bind() {
        if (subimage == null) {
            this.subimage = Textures.image(path);
            this.width = subimage.getWidth();
            this.height = subimage.getHeight();
        }
        subimage.setImageColor(r, g, b, a);
        subimage.bind();
    }

    public static void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

}
