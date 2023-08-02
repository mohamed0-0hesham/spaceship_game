package com.hesham0_0.spaceship.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Wall {
    private static final float DENSITY = 1f;
    private static final float FRICTION = 1f;
    private static final float RESTITUTION = 1f;
    private final Body body;
    public String direction;


    public Wall(World world, Vector2 position, float height, float width, String direction) {
        this.direction=direction;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width,height);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
        fixtureDef.restitution = RESTITUTION;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);

        MassData massData = new MassData();
        massData.mass = 10000f;
        body.setMassData(massData);
        shape.dispose();
    }

    public void update() {

    }

    public void dispose() {
        body.getWorld().destroyBody(body);
    }

    public Body getBody() {
        return body;
    }
}

