package com.hesham0_0.spaceship;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class SpaceshipUtils {
    public static int MAX_PRESSURE = 100;
    public static long MILLIS_IN_SECOND = 1000;
    public static int SQUEEZE_THRESHOLD = 15;
    public static float colorAttr1 = 1f;
    public static float colorAttr2 = 0.5f;
    public static float RGB_COLOR_COEFFICIENT = 1 / 256f;
    public static float[] RING_START_LEVEL = {1, 33, 67};
    public static float[] RING_FULL_VAlUE_LEVEL = {33, 34, 33};
    public static Color[] RINGS_COLORS = {
            new Color(100 * RGB_COLOR_COEFFICIENT, 148 * RGB_COLOR_COEFFICIENT, 237 * RGB_COLOR_COEFFICIENT, 0),
            new Color(66 * RGB_COLOR_COEFFICIENT, 134 * RGB_COLOR_COEFFICIENT, 244 * RGB_COLOR_COEFFICIENT, 0),
            new Color(38 * RGB_COLOR_COEFFICIENT, 67 * RGB_COLOR_COEFFICIENT, 139 * RGB_COLOR_COEFFICIENT, 0)
    };
    public static Color[] ROCKS_COLORS = {
            new Color(colorAttr1, colorAttr2, colorAttr2, 1f),
            new Color(colorAttr1, colorAttr1, colorAttr2, 1f),
            new Color(colorAttr2, colorAttr1, colorAttr1, 1f),
            new Color(colorAttr1, colorAttr2, colorAttr2, 1f),
            new Color(colorAttr2, colorAttr1, colorAttr2, 1f),
            new Color(colorAttr2, colorAttr2, colorAttr1, 1f)
    };

    // ---------------------(game speed setting)--------------------
    public static final float rockSpeed = 200 * 1f;
    public static final float bulletSpeed = 200 * 4f;
    public static final float explosionAnimationTime = 0.8f;

    //-----------------------------------------------------------

    public static String getTextureNumberPath(int health){
        return "spaceshipGame/spaceship_num_"+health+".png";
    }

    public static String getTextureExplosionNumberPath(String signal,int score){
        return "spaceshipGame/spaceship_num_"+signal+score+".png";
    }

    public static String getRandomRockPath(int level) {
        Random random = new Random();
        int randomShape = random.nextInt(9) + 1;
        return "spaceshipGame/rocks_level_" + level + "/" + randomShape + ".png";
    }
//    public static String getRandomItem() {
//        Random random = new Random();
//        int randomShape = random.nextInt(9) + 1;
//        return "spaceshipGame/rocks_level_" + level + "/" + randomShape + ".png";
//    }
    public static float getRandomRockRotation() {
        Random random = new Random();
        return (float) (random.nextInt(3) * 5);
    }

    public static Color getRandomRockColor() {
        Random random = new Random();
        int randomColor = random.nextInt(5);
        return ROCKS_COLORS[randomColor];
    }
}
