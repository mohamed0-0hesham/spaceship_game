package com.hesham0_0.spaceship.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Ring {
    private static final float DENSITY = 1.0f;
    private static final float FRICTION = 0.4f;
    private static final float RESTITUTION = 0.6f;
    private float alpha;
    private Vector2 position;
    private float radius;
    private int level;
    private Color color;
    private int thickness=100;
    private Body body;
    private World world;

    public Ring(World world, Vector2 position, float alpha, float radius,int level) {
        this.world = world;
        this.position = position;
        this.alpha = alpha;
        this.radius = radius;
        this.level=level;

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
        fixtureDef.restitution = RESTITUTION;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);

        shape.dispose();
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (level==0){
            color = new Color(1, 0, 0, alpha/1000);
        }else if (level==1){
            color = new Color(0, 1, 0, alpha/1000);
        }else {
            color = new Color(0, 0, 1, alpha/1000);
        }
        shapeRenderer.setColor(color);
        shapeRenderer.circle(position.x, position.y, radius, thickness);
    }

    public void update(float alpha,float radius) {
        this.radius=radius;
        this.alpha=alpha;
        Gdx.app.log("MainGame","update 3="+radius+" "+alpha);
    }

    public void dispose() {
        body.getWorld().destroyBody(body);
    }
}
