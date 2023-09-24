package com.hesham0_0.spaceship;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class SpaceshipUtils {
    public static int MAX_PRESSURE = 100;
    public static long MILLIS_IN_SECOND = 1000;
    public static int SQUEEZE_THRESHOLD = 15;
    public static float colorAttr1 = 1f;
    public static float colorAttr2 = 0.5f;
    public static Color[] ROCKS_COLORS = {
            new Color(colorAttr1, colorAttr2, colorAttr2, 1f),
            new Color(colorAttr1, colorAttr1, colorAttr2, 1f),
            new Color(colorAttr2, colorAttr1, colorAttr1, 1f),
            new Color(colorAttr1, colorAttr2, colorAttr2, 1f),
            new Color(colorAttr2, colorAttr1, colorAttr2, 1f),
            new Color(colorAttr2, colorAttr2, colorAttr1, 1f)
    };
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
