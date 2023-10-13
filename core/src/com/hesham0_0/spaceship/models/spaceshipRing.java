package com.hesham0_0.spaceship.models;

import static com.hesham0_0.spaceship.SpaceshipUtils.RINGS_COLORS;
import static com.hesham0_0.spaceship.SpaceshipUtils.RING_FULL_VAlUE_LEVEL;
import static com.hesham0_0.spaceship.SpaceshipUtils.RING_START_LEVEL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;

public class spaceshipRing {
    private static final float DENSITY = 0f;
    private static final float FRICTION = 0f;
    private static final float RESTITUTION = 0f;
    private final float radius;
    private final ShapeRenderer shapeRenderer;
    private final int level;
    private final Body body;
    private final CircleShape shape;
    private final Color color;
    private boolean isAlive=true;

    public spaceshipRing(World world, Vector2 position, float radius, ShapeRenderer shapeRenderer, int level, boolean forceField) {
        this.radius = radius;
        this.level=level;
        this.shapeRenderer=shapeRenderer;

        shape = new CircleShape();
        shape.setRadius(radius);
        color = RINGS_COLORS[level];

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
        fixtureDef.restitution = RESTITUTION;
        fixtureDef.isSensor = !forceField;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);

        MassData massData = new MassData();
        massData.mass = 0f;
        body.setMassData(massData);
    }

    public void render(SpriteBatch batch) {
        drawBodyPoints(batch);
    }

    public void update(float pressure) {
        float alpha = Math.min((pressure - RING_START_LEVEL[level]) / RING_FULL_VAlUE_LEVEL[level], 1);
        color.a = alpha;
    }
    public void drawBodyPoints(SpriteBatch batch){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.setAutoShapeType(true);
        Gdx.gl.glLineWidth(15);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(color);
        Vector2 vertexPos = body.getPosition();
        shapeRenderer.circle(vertexPos.x, vertexPos.y, radius,50);
        shapeRenderer.end();
    }

    public void dispose() {
        shape.dispose();
        body.getWorld().destroyBody(body);
    }

    public void die() {
        isAlive=false;
        // Remove the Rock object from the game
    }
    public boolean isAlive(){
        return isAlive;
    }
    public Body getBody() {
        return body;
    }
}
