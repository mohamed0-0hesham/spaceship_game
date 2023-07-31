package com.hesham0_0.spaceship.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
        bodyDef.position.set(virtualWidth / 2f , virtualHeight / 2f );

        // Create the spaceship body
        spaceshipBody = world.createBody(bodyDef);

        // Create the spaceship shape
        PolygonShape shape = new PolygonShape();

        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(0, spaceshipTexture.getHeight() / 2f);
        vertices[1] = new Vector2(-spaceshipTexture.getWidth() / 2f, -spaceshipTexture.getHeight() / 2f);
        vertices[2] = new Vector2(spaceshipTexture.getWidth() / 2f, -spaceshipTexture.getHeight() / 2f);
        shape.set(vertices);

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

        targetAngle= currentAngle = (float) Math.toRadians(90);
        spaceshipBody.setUserData(this);

    }

    public void update(float delta) {
        updateRotation(delta);
    }

    public void setTargetAngle(float targetAngle) {
        this.targetAngle = targetAngle;
    }
    public Float getTargetAngle() {
        return targetAngle;
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
    }


    public void render(SpriteBatch batch) {
        batch.draw(spaceshipTexture,
                spaceshipBody.getPosition().x - spaceshipTexture.getWidth() / 2f,
                spaceshipBody.getPosition().y - spaceshipTexture.getHeight() / 2f,
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

        drawBodyPoints(batch);
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

    public void drawBodyPoints(SpriteBatch batch){
        // Get the vertices of the spaceship shape
        PolygonShape shape = (PolygonShape) spaceshipBody.getFixtureList().get(0).getShape();
        int vertexCount = shape.getVertexCount();
        Vector2[] vertices = new Vector2[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            Vector2 vertex = new Vector2();
            shape.getVertex(i, vertex);
            vertices[i] = vertex;
        }

        // Draw circles at each vertex of the spaceship shape
        batch.end();
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        for (int i = 0; i < vertexCount; i++) {
            Vector2 vertex = vertices[i];
            Vector2 vertexPos = spaceshipBody.getWorldPoint(vertex);
            shapeRenderer.circle(vertexPos.x, vertexPos.y, 5);
        }
        shapeRenderer.end();
        batch.begin();
    }
}
