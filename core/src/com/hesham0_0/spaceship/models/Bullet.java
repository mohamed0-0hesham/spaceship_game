package com.hesham0_0.spaceship.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet {
    private Body body;
    private Color color;
    private Vector2 speed;
    private Texture bulletTexture;
    private boolean isAlive=true;

    public Bullet(World world, float x, float y, float width, float height) {
        bulletTexture = new Texture("bullet.png");

        // Create the body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
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

    public void setSpeed(float speedX, float speedY) {
        speed=new Vector2(speedX,speedY);
    }


    public void update() {
        Vector2 position = body.getPosition().add(speed);
        body.setTransform(position, position.angleRad());
    }

    public void render(SpriteBatch batch) {
        batch.draw(bulletTexture, body.getPosition().x - bulletTexture.getWidth() / 2f,
                body.getPosition().y - bulletTexture.getHeight() / 2f);
    }

    public void dispose() {
        // Dispose of the body when the bullet is no longer needed
        bulletTexture.dispose();
        body.getWorld().destroyBody(body);
    }

    public Body getBody() {
        return body;
    }

    public void die() {
        isAlive=false;
        // Remove the Rock object from the game
    }
    public boolean isAlive(){
        return isAlive;
    }
}
