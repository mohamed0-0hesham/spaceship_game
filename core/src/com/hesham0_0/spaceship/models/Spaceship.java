package com.hesham0_0.spaceship.models;

import static com.hesham0_0.spaceship.SpaceshipState.STABLE;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.hesham0_0.spaceship.SpaceshipState;

public class Spaceship {
    private final Texture spaceshipTexture;
    private final Body spaceshipBody;
    private Float targetAngle;
    public float currentAngle;
    private float interpolationSpeed;
    public float alpha = 0;
    public float signal = 1;
    public SpaceshipState state = STABLE;
    private Array<TextureRegion> pieceTextures;
    private Array<Vector2> piecePositions;
    private Array<Vector2> pieceVelocities;
    private float piecesAlpha = 1;


    public Spaceship(World world, float virtualWidth, float virtualHeight) {
        spaceshipTexture = new Texture("spaceshipGame/spaceship.png");

        // Create the spaceship body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(virtualWidth / 2f, virtualHeight / 2f);

        // Create the spaceship body
        spaceshipBody = world.createBody(bodyDef);
        // Create the spaceship shape
        PolygonShape shape = new PolygonShape();
        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(0, spaceshipTexture.getHeight() / 2f);
        vertices[1] = new Vector2(-spaceshipTexture.getWidth() / 2f, -spaceshipTexture.getHeight() / 2f);
        vertices[2] = new Vector2(spaceshipTexture.getWidth() / 2f, -spaceshipTexture.getHeight() / 2f);
        shape.set(vertices);

        shape.setRadius(spaceshipTexture.getWidth() / 2f);

        // Create the spaceship fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;
        fixtureDef.isSensor = true;

        // Create the spaceship fixture
        spaceshipBody.createFixture(fixtureDef);

        // Dispose of the shape
        shape.dispose();

        targetAngle = currentAngle = (float) Math.toRadians(90);
        spaceshipBody.setUserData(this);

    }

    public void update(float delta) {
        updateRotation(delta);
        if (state == SpaceshipState.BLINKS) {
            updateAlpha(delta);
        } else {
            alpha = 1;
        }
        if (state == SpaceshipState.DESTROYED) {
            spreadPieces(delta);
        }
    }

    public void setTargetAngle(float targetAngle) {
        interpolationSpeed = 10f;
        this.targetAngle = targetAngle;
    }

    public void setDraggingAngle(float targetAngle) {
        interpolationSpeed = 50f;
        this.targetAngle = targetAngle;
    }

    private void updateRotation(float delta) {
        float angleDiff = targetAngle - currentAngle;

        // Ensure the angle difference is within -180 to 180 degrees
        if (angleDiff < -MathUtils.PI)
            angleDiff += MathUtils.PI2;
        else if (angleDiff > MathUtils.PI)
            angleDiff -= MathUtils.PI2;

        interpolationSpeed = 10f;

        currentAngle += angleDiff * interpolationSpeed * delta;
        spaceshipBody.setTransform(spaceshipBody.getPosition(), (float) (currentAngle - Math.toRadians(90)));
    }

    private void updateAlpha(float delta) {
        float interpolationSpeed = 10f;
        if (alpha > 1) {
            signal = -1;
        } else if (alpha < 0) {
            signal = 1;
        }
        alpha += signal * interpolationSpeed * delta;
    }


    public void render(SpriteBatch batch) {
        if (state != SpaceshipState.DESTROYED) {
            batch.setColor(new Color(1, 1, 1, alpha));
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
            batch.setColor(Color.WHITE);
        } else {
            renderPieces(batch);
        }

    }

    private void renderPieces(SpriteBatch batch) {
        batch.setColor(new Color(1, 1, 1, piecesAlpha));
        for (int i = 0; i < pieceTextures.size; i++) {
            TextureRegion pieceTexture = pieceTextures.get(i);
            Vector2 piecePosition = piecePositions.get(i);

            batch.draw(
                    pieceTexture,
                    piecePosition.x,
                    piecePosition.y,
                    pieceTexture.getRegionWidth() / 2f,
                    pieceTexture.getRegionHeight() / 2f,
                    pieceTexture.getRegionWidth(),
                    pieceTexture.getRegionHeight(),
                    1f,
                    1f,
                    0
            );
        }
        batch.setColor(Color.WHITE);
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


    public Polygon getSensorShape() {
        Polygon polygon = new Polygon();
        PolygonShape shape = (PolygonShape) spaceshipBody.getFixtureList().get(spaceshipBody.getFixtureList().size - 1).getShape();

        int vertexCount = shape.getVertexCount();
        float[] floatVertices = new float[vertexCount * 2];

        for (int i = 0; i < vertexCount; i++) {
            Vector2 vertex = new Vector2();
            shape.getVertex(i, vertex);

            float cosAngle = MathUtils.cos((float) (currentAngle + Math.toRadians(-90)));
            float sinAngle = MathUtils.sin((float) (currentAngle + Math.toRadians(-90)));
            float rotatedX = cosAngle * vertex.x - sinAngle * vertex.y;
            float rotatedY = sinAngle * vertex.x + cosAngle * vertex.y;

            floatVertices[i * 2] = rotatedX + spaceshipBody.getPosition().x;
            floatVertices[i * 2 + 1] = rotatedY + spaceshipBody.getPosition().y;
        }

        polygon.setVertices(floatVertices);
        return polygon;
    }

    public void createPieces(float rockAngle) {
        pieceTextures = new Array<>();
        piecePositions = new Array<>();
        pieceVelocities = new Array<>();
        int rows = 7;
        int columns = 14;
        int pieceWidth = spaceshipTexture.getWidth() / columns;
        int pieceHeight = spaceshipTexture.getHeight() / rows;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int startX = col * pieceWidth;
                int startY = row * pieceHeight;
                createPieceTextureRegion(startX, startY, pieceWidth, pieceHeight);
                addPiecePosition(startX, startY, pieceHeight);
                addPieceVelocity(rockAngle);
            }
        }
    }

    private void createPieceTextureRegion(int startX, int startY, int pieceWidth, int pieceHeight) {
        TextureRegion pieceTextureRegion = new TextureRegion(spaceshipTexture, startX, startY, pieceWidth, pieceHeight);
        pieceTextures.add(pieceTextureRegion);
    }

    private void addPiecePosition(int startX, int startY, int pieceHeight) {
        float cosAngle = MathUtils.cos((float) (currentAngle - Math.toRadians(90)));
        float sinAngle = MathUtils.sin((float) (currentAngle - Math.toRadians(90)));

        float relativeX = startX - spaceshipTexture.getWidth() / 2f;
        float relativeY = spaceshipTexture.getHeight() - (startY + pieceHeight) - spaceshipTexture.getHeight() / 2f;

        float rotatedX = cosAngle * relativeX - sinAngle * relativeY;
        float rotatedY = sinAngle * relativeX + cosAngle * relativeY;

        float pieceX = spaceshipBody.getPosition().x + rotatedX;
        float pieceY = spaceshipBody.getPosition().y + rotatedY;

        Vector2 piecePosition = new Vector2(pieceX, pieceY);
        piecePositions.add(piecePosition);
    }

    private void addPieceVelocity(float rockAngle) {
        float speed = MathUtils.random(50f, 100f);
        float randomAngle = rockAngle + MathUtils.random((float) Math.toRadians(-30f), (float) Math.toRadians(30f));
        float randomVelX = speed * MathUtils.cos(randomAngle);
        float randomVelY = speed * MathUtils.sin(randomAngle);
        Vector2 initialVelocity = new Vector2(randomVelX, randomVelY);
        pieceVelocities.add(initialVelocity);
    }

    private void spreadPieces(float delta) {
        float interpolationSpeed = 0.5f;
        float Speed = 2f;
        piecesAlpha -= delta * interpolationSpeed;
        for (int i = 0; i < piecePositions.size; i++) {
            Vector2 piecePosition = piecePositions.get(i);
            piecePosition.add(pieceVelocities.get(i).x * delta * Speed, pieceVelocities.get(i).y * delta * Speed);
        }
    }

    public void resetPiecesAlpha() {
        piecesAlpha = 1;
    }
}
