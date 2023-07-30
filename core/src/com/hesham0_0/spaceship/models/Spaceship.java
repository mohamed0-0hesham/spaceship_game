package com.hesham0_0.spaceship.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Spaceship {
    private final Texture spaceshipTexture;
    private final Body spaceshipBody;
    private Float targetAngle;
    private float currentAngle;
    public Spaceship(World world, float virtualWidth, float virtualHeight) {
        spaceshipTexture = new Texture("spaceship.png");

        // Create the spaceship body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(virtualWidth / 2f - spaceshipTexture.getWidth() / 2f, virtualHeight / 2f - spaceshipTexture.getHeight() / 2f);

        // Create the spaceship body
        spaceshipBody = world.createBody(bodyDef);

        // Create the spaceship shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(spaceshipTexture.getWidth() / 2f, spaceshipTexture.getHeight() / 2f);

        // Create the spaceship fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        // Create the spaceship fixture
        spaceshipBody.createFixture(fixtureDef);

        // Dispose of the shape
        shape.dispose();

        targetAngle= currentAngle = spaceshipBody.getPosition().angleRad();

    }

    public void update(float delta) {
        updateRotation(delta);
    }

    public void setTargetAngle(float targetAngle) {
        this.targetAngle = targetAngle;
        Gdx.app.log("Spaceship", "angle: " + Math.toDegrees(targetAngle));
    }

    private void updateRotation(float delta) {
        float angleDiff = targetAngle - currentAngle;

        // Ensure the angle difference is within -180 to 180 degrees
        if (angleDiff < -MathUtils.PI)
            angleDiff += MathUtils.PI2;
        else if (angleDiff > MathUtils.PI)
            angleDiff -= MathUtils.PI2;

        float interpolationSpeed = 10f;

        currentAngle += angleDiff * interpolationSpeed * delta;
        spaceshipBody.setTransform(spaceshipBody.getPosition(), (float) (currentAngle- Math.toRadians(90)));
        // Set the new angle for the spaceship's physics body
//        float newAngle = (float) (spaceshipBody.getAngle() + angleDiff - Math.toRadians(90));
//        spaceshipBody.setTransform(spaceshipBody.getPosition(), newAngle);
    }


    public void render(SpriteBatch batch) {
        batch.draw(spaceshipTexture,
                spaceshipBody.getPosition().x, spaceshipBody.getPosition().y,
                spaceshipTexture.getWidth() / 2f,
                spaceshipTexture.getHeight() / 2f,
                spaceshipTexture.getWidth(),
                spaceshipTexture.getHeight(),
                1f, 1f,
                spaceshipBody.getAngle() * MathUtils.radiansToDegrees,
                0, 0,
                spaceshipTexture.getWidth(),
                spaceshipTexture.getHeight(),
                false,
                false);
    }

    public Body getBody() {
        return spaceshipBody;
    }
    public Texture getTexture() {
        return spaceshipTexture;
    }

    public void dispose() {
        spaceshipTexture.dispose();
    }
}
