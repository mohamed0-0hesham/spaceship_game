package com.hesham0_0.spaceship.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Explosion {
    private Texture explosionTexture;
    private float frameWidth, frameHeight;
    private float x, y;
    private float size;
    private float alpha;
    private float stateTime;
    private final float durationTime = 0.5f;
    private boolean isAlive = true;

    public Explosion(Vector2 position) {
        explosionTexture = new Texture("explosion.png");
        this.frameWidth = explosionTexture.getWidth();
        this.frameHeight = explosionTexture.getHeight();
        this.x = position.x;
        this.y = position.y;
        this.stateTime = 0;
        alpha = 1;
        size = 1;
    }

    public void update(float delta) {
        stateTime += delta;
        size = stateTime*2;
        if (stateTime > durationTime) {
            isAlive = false;
        }
        Gdx.app.log("Explosion", "Explosion stateTime = " + stateTime);
    }

    public void render(SpriteBatch batch) {
        float centerX = x - frameWidth * size / 2f;
        float centerY = y - frameHeight * size / 2f;
        batch.setColor(1f, 1f, 1f, alpha);
        batch.draw(explosionTexture, centerX, centerY, frameWidth * size, frameHeight * size);
        batch.setColor(Color.WHITE);
    }

    public void dispose() {
        explosionTexture.dispose();
    }

    public boolean isAlive() {
        return isAlive;
    }
}