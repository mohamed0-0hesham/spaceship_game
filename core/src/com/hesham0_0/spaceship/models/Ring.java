package com.hesham0_0.spaceship.models;

import static com.hesham0_0.spaceship.Main.RGB_COLOR_COEFFICIENT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;

public class Ring {
    private static final float DENSITY = 0f;
    private static final float FRICTION = 0f;
    private static final float RESTITUTION = 0f;
    private float alpha;
    private Spaceship spaceship;
    private float radius;
    private ShapeRenderer shapeRenderer;
    private int level;
    private Color color;
    private Body body;
    private World world;
    private CircleShape shape;
    private FixtureDef fixtureDef;

    private boolean isAlive=true;

    public Ring(World world, Spaceship spaceship, float alpha, float radius, int level) {
        this.world = world;
        this.spaceship = spaceship;
        this.alpha = alpha;
        this.radius = radius;
        this.level=level;

        shapeRenderer = new ShapeRenderer();

        shape = new CircleShape();
        shape.setRadius(radius);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
        fixtureDef.restitution = RESTITUTION;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(spaceship.getBody().getPosition());

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);

        MassData massData = new MassData();
        massData.mass = 0f;
        body.setMassData(massData);
    }

    public void render(SpriteBatch batch) {
        if (level==0){
            color = new Color(100*RGB_COLOR_COEFFICIENT, 148*RGB_COLOR_COEFFICIENT, 237*RGB_COLOR_COEFFICIENT, alpha/1000);
        }else if (level==1){
            color = new Color(66*RGB_COLOR_COEFFICIENT, 134*RGB_COLOR_COEFFICIENT, 244*RGB_COLOR_COEFFICIENT, alpha/1000);
        }else {
            color = new Color(38*RGB_COLOR_COEFFICIENT, 67*RGB_COLOR_COEFFICIENT, 139*RGB_COLOR_COEFFICIENT, alpha/1000);
        }
        drawBodyPoints(batch,color);
    }

    public void update(float alpha) {
        this.alpha=alpha;
    }
    public void drawBodyPoints(SpriteBatch batch, Color color){
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
        shapeRenderer.dispose();
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
