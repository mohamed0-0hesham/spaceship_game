package com.hesham0_0.spaceship.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.hesham0_0.spaceship.SpaceshipUtils;

public class SpaceshipExplosion {
    private final Texture explosionTexture;
    private final float frameWidth;
    private final float frameHeight;
    private final float x;
    private final float y;
    private float size;
    private final float alpha;
    private float stateTime;
    private final float durationTime;
    private boolean isAlive = true;
    private final Texture numTexture;
    private final int score;

    public SpaceshipExplosion(Vector2 position, float durationTime, String signal, int score) {
        explosionTexture = new Texture("spaceshipGame/explosion.png");
        this.frameWidth = explosionTexture.getWidth();
        this.frameHeight = explosionTexture.getHeight();
        this.x = position.x;
        this.y = position.y;
        this.stateTime = 0;
        this.durationTime=durationTime;
        this.score = score;
        alpha = 1;
        size = 0;

        numTexture = new Texture(SpaceshipUtils.getTextureExplosionNumberPath(signal,score));
    }

    public void update(float delta) {
        stateTime += delta;
        float timeRatio = stateTime / durationTime;
        float angle = 180 * timeRatio;
        size = (float) Math.sin(Math.toRadians(angle));
        if (stateTime > durationTime) {
            isAlive = false;
        }
    }

    public void render(SpriteBatch batch) {
        float centerX = x - frameWidth * size / 2f;
        float centerY = y - frameHeight * size / 2f;
        batch.setColor(1f, 1f, 1f, alpha);
        batch.draw(explosionTexture, centerX, centerY, frameWidth * size, frameHeight * size);
        batch.setColor(Color.WHITE);

        float centerNumX = x - numTexture.getWidth() * size / 2f;
        float centerNumY = y - numTexture.getHeight() * size / 2f;

        if (score != 0) {
            batch.draw(numTexture, centerNumX, centerNumY + numTexture.getHeight() / 2f, numTexture.getWidth() * size, numTexture.getHeight() * size);
        }
    }

    public void dispose() {
        explosionTexture.dispose();
    }

    public boolean isAlive() {
        return isAlive;
    }
}
