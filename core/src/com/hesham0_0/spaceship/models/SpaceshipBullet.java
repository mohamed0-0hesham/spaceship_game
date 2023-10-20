package com.hesham0_0.spaceship.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class SpaceshipBullet {
    private final Body body;
    private final float angle;
    private Vector2 speed;
    private final Texture bulletTexture;
    private boolean isAlive = true;
    private int bulletPower = 1;

    public SpaceshipBullet(World world, float x, float y, float width, float height, float angle, int bulletPower) {
        this.bulletPower = bulletPower;
        this.angle = (float) (angle - Math.toRadians(90));
        bulletTexture = new Texture("spaceshipGame/bullet.png");

        // Create the body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);

        // Create the bullet body
        body = world.createBody(bodyDef);

        // Create the shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        // Create the fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0;

        // Create the fixture
        body.createFixture(fixtureDef);

        // Dispose of the shape
        shape.dispose();
        MassData massData = new MassData();
        massData.mass = 0.0000005f;
        body.setMassData(massData);
        body.setBullet(true);
        body.setUserData(this);
    }

    public void setSpeed(float angle, float magnitude) {
        float speedX = (float) Math.cos(angle) * magnitude;
        float speedY = (float) Math.sin(angle) * magnitude;
        speed = new Vector2(speedX, speedY);
    }


    public void update(float delta) {
        Vector2 distance = new Vector2(speed.x * delta, speed.y * delta);
        Vector2 position = body.getPosition().add(distance);
        body.setTransform(position, angle);
    }

    public void render(SpriteBatch batch) {
        batch.draw(bulletTexture,
                body.getPosition().x - bulletTexture.getWidth() / 2f,
                body.getPosition().y - bulletTexture.getHeight() / 2f,
                bulletTexture.getWidth() / 2f,
                bulletTexture.getHeight() / 2f,
                bulletTexture.getWidth(),
                bulletTexture.getHeight(),
                1f, 1f,
                angle * MathUtils.radiansToDegrees,
                0, 0,
                bulletTexture.getWidth(),
                bulletTexture.getHeight(),
                false,
                false);
    }

    public void dispose() {
        // Dispose of the body when the bullet is no longer needed
        bulletTexture.dispose();
        body.getWorld().destroyBody(body);
    }

    public Body getBody() {
        return body;
    }

    public int getBulletPower() {
        return bulletPower;
    }

    public void die() {
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }
}
