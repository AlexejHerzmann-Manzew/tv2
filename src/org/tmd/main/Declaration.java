/*
 *  Authors:
 *     Whizzpered,
 *     Yew_Mentzaki.
 */
package org.tmd.main;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.tmd.environment.Block;
import static org.tmd.environment.Block.BLOCK_HEIGHT;
import static org.tmd.environment.Block.BLOCK_WIDTH;
import org.tmd.environment.Point;
import org.tmd.environment.WaterBlock;
import org.tmd.environment.entities.Gate;
import org.tmd.environment.entities.NPC;
import static org.tmd.main.Main.RANDOM;
import org.tmd.render.Image;
import org.tmd.render.scenes.*;

/**
 *
 * @author yew_mentzaki
 */
public class Declaration {
    /*
     *   Scenes:
     */

    public static MainMenu mainMenu = new MainMenu();
    public static Settings settings = new Settings();
    public static Dungeon dungeon = null;
    public static Dialog dialog = new Dialog();

    /*
     *   Blocks:
     */
    public static Block blockAir = new Block(0, ' ', new Color(0, 0, 0, 0), null, true, false, false);
    public static Block blockFloor = new Block(1, '_', new Color(255, 255, 255, 100), "floor.png", false, false, false);
    public static Block blockWall = new Block(2, '*', new Color(0, 0, 0, 100), "wall.png", "top.png", "border.png", "border_angle.png", "border_angle_inside.png", true, false, false);
    public static Block blockWater = new WaterBlock(3, '~', "water.png", "border.png", "border_angle.png", "border_angle_inside.png", true, false, false);
    public static Block blockPlayer = new Block(4, 'P', new Color(255, 255, 255, 100), "floor.png", false, false, false) {
        @Override
        public void parserAction(Dungeon dungeon, double x, double y) {
            dungeon.playerRespawnPoint = new Point(x, y);
        }
    };
    public static Block blockEnemy = new Block(5, '|', new Color(255, 255, 255, 100), "floor.png", false, false, false) {
        @Override
        public void parserAction(Dungeon dungeon, double x, double y) {
            dungeon.raidersRespawnPoint = new Point(x, y);
        }
    };
    public static Block blockMinion = new Block(6, '@', new Color(255, 255, 255, 100), "floor.png", false, false, false) {
        @Override
        public void parserAction(Dungeon dungeon, double x, double y) {
            dungeon.minionsRespawnPoints.add(new Point(x, y));
        }
    };

    public static Block blockGate = new Block(7, '!', new Color(255, 255, 255, 100), "floor.png", false, false, false) {
        @Override
        public void parserAction(Dungeon dungeon, double x, double y) {
            Gate gate = new Gate(x, y);
            gate.dungeon = dungeon;
            dungeon.entities.add(gate);
        }
    };

    public static Block blockRestZone = new Block(8, '+', new Color(255, 255, 255, 100), "floor.png", false, false, true);
    public static Block blockEnemyZone = new Block(9, '-', new Color(255, 255, 255, 100), "floor.png", false, true, false);

    public static Block blockSeller = new Block(10, '$', new Color(255, 255, 255, 100), "floor.png", false, false, true) {

        @Override
        public void parserAction(Dungeon dungeon, double x, double y) {
            NPC npc = new NPC(x, y, "creatures/seller/anim", "Seller", "seller.dialog");
            npc.dungeon = dungeon;
            dungeon.entities.add(npc);
        }

    };

    public static Block blockMaster = new Block(11, 'M', new Color(255, 255, 255, 100), "floor.png", false, false, true) {

        @Override
        public void parserAction(Dungeon dungeon, double x, double y) {
            NPC npc = new NPC(x, y, "creatures/sensei/anim", "Sensei", "seller.dialog");
            npc.dungeon = dungeon;
            dungeon.entities.add(npc);
        }

    };

}
