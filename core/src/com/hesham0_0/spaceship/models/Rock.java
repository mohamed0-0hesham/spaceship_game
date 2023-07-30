package com.hesham0_0.spaceship.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;

public class Rock {
    private Vector2 speed;
    private Texture rockTexture;
    public static int rockRadius;
    Texture[] rockTextures = {new Texture("rock1.png"), new Texture("rock2.png"), new Texture("rock3.png")};

    public float x;
    public float y;
    public float angle;
    public float level;
    private Body rockBody;

    public Rock(World world, int level, float x, float y) {
        this.x = x;
        this.y = y;
        this.level = level;
        rockTexture = rockTextures[level - 1];
        rockRadius = rockTexture.getWidth() / 2;

        BodyDef rockBodyDef = new BodyDef();
        rockBodyDef.type = BodyDef.BodyType.DynamicBody;
        rockBodyDef.position.set(x, y);
//        rockBodyDef.angle = angle;

        CircleShape shape = new CircleShape();
        shape.setRadius(rockRadius);

        FixtureDef rockFixture = new FixtureDef();
        rockFixture.shape = shape;
        rockFixture.density = 0.00005f;
        rockFixture.friction = 0.001f;
        rockFixture.restitution = 0.001f;
        rockBody = world.createBody(rockBodyDef);
        rockBody.createFixture(rockFixture);

        rockBody.setTransform(x, y, 0);
        MassData massData = new MassData();
        massData.mass = 0.00005f;
        massData.center.set(0.0f, 0.0f);
        massData.I = (0.15f);
        rockBody.setMassData(massData);

        rockBody.setUserData(this);

    }

    public void setSpeed(float magnitude, float angle) {
        float speedX = (float) Math.cos(angle) * magnitude;
        float speedY = (float) Math.sin(angle) * magnitude;
        speed=new Vector2(speedX,speedY);
    }
    public void update() {
        Vector2 position = rockBody.getPosition().add(speed);
        rockBody.setTransform(position, position.angleRad());
    }

    public void render(SpriteBatch batch) {
        batch.draw(rockTexture, rockBody.getPosition().x - rockTexture.getWidth() / 2f,
                rockBody.getPosition().y - rockTexture.getHeight() / 2f);
    }

    public void dispose() {
        // Dispose of the body when the bullet is no longer needed
        rockTexture.dispose();
        rockBody.getWorld().destroyBody(rockBody);
    }

    public Body getBody() {
        return rockBody;
    }

    public void die() {
        rockBody.getWorld().destroyBody(rockBody);
        // Remove the Rock object from the game
    }
}
