package com.hesham0_0.spaceship.models;

import static com.hesham0_0.spaceship.SpaceshipUtils.getRandomRockPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.hesham0_0.spaceship.SpaceshipUtils;

public class RockFragments {
    private final Texture rockTexture;
    private final float x;
    private final float y;
    private float alpha;
    private float size;
    private float stateTime;
    private final float durationTime;
    private boolean isAlive = true;
    private final Texture numTexture;
    private Array<Texture> pieceTextures ;
    private  Array<Vector2> piecePositions ;
    private  Array<Vector2> pieceVelocities ;
    private final SpaceshipRock rock;
    private final int score;

    public RockFragments(float durationTime, String signal, int score, SpaceshipRock rock) {
        this.rockTexture = rock.getTexture();
        this.x = rock.getBody().getPosition().x;
        this.y = rock.getBody().getPosition().y;
        this.rock = rock;
        this.stateTime = 0;
        this.durationTime = durationTime;
        this.score = score;
        alpha = 1;
        size = 0;
        numTexture = new Texture(SpaceshipUtils.getTextureExplosionNumberPath(signal, score));
        createPieces();
    }

    public void update(float delta) {
        stateTime += delta;
        float timeRatio = stateTime / durationTime;
        float angle = 180 * timeRatio;
        size = (float) Math.sin(Math.toRadians(angle));
        if (stateTime > durationTime) {
            isAlive = false;
        }
        spreadPieces(delta);
    }

    public void render(SpriteBatch batch) {
        renderPieces(batch);
        if (score != 0) {
            float centerNumX = x - numTexture.getWidth() * size / 2f;
            float centerNumY = y - numTexture.getHeight() * size / 2f;
            batch.draw(numTexture, centerNumX, centerNumY + numTexture.getHeight() / 2f, numTexture.getWidth() * size, numTexture.getHeight() * size);
        }
    }

    public void dispose() {
        rockTexture.dispose();
        for (Texture texture:pieceTextures) {
            texture.dispose();
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    private void renderPieces(SpriteBatch batch) {
        batch.setColor(rock.color.r,rock.color.g, rock.color.b, alpha);
        for (int i = 0; i < pieceTextures.size; i++) {
            Texture pieceTexture = pieceTextures.get(i);
            Vector2 piecePosition = piecePositions.get(i);
            batch.draw(
                    pieceTexture,
                    piecePosition.x,
                    piecePosition.y,
                    pieceTexture.getWidth() / 4f,
                    pieceTexture.getHeight() / 4f
            );
        }
        batch.setColor(Color.WHITE);
    }

    public void createPieces() {
        pieceTextures = new Array<>();
        piecePositions = new Array<>();
        pieceVelocities = new Array<>();
        int rows = 3 ;
        int columns = 3 ;
        int pieceWidth = rockTexture.getWidth() / columns;
        int pieceHeight = rockTexture.getHeight() / rows;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int startX = col * pieceWidth;
                int startY = row * pieceHeight;
                Texture rockTexture = new Texture(getRandomRockPath(1));
                pieceTextures.add(rockTexture);
                addPiecePosition(startX, startY, pieceHeight);
                addPieceVelocity();
            }
        }
    }


    private void addPiecePosition(int startX, int startY, int pieceHeight) {
        float cosAngle = MathUtils.cos((float) (rock.rotationAngle - Math.toRadians(90)));
        float sinAngle = MathUtils.sin((float) (rock.rotationAngle - Math.toRadians(90)));

        float relativeX = startX - rockTexture.getWidth() / 2f;
        float relativeY = rockTexture.getHeight() - (startY + pieceHeight) - rockTexture.getHeight() / 2f;

        float rotatedX = cosAngle * relativeX - sinAngle * relativeY;
        float rotatedY = sinAngle * relativeX + cosAngle * relativeY;

        float pieceX = rock.getBody().getPosition().x + rotatedX;
        float pieceY = rock.getBody().getPosition().y + rotatedY;

        Vector2 piecePosition = new Vector2(pieceX, pieceY);
        piecePositions.add(piecePosition);
    }

    private void addPieceVelocity() {
        float speed = MathUtils.random(80f, 150f);
        float randomAngle = MathUtils.random((float) Math.toRadians(-180f), (float) Math.toRadians(180f));
        float randomVelX = speed * MathUtils.cos(randomAngle);
        float randomVelY = speed * MathUtils.sin(randomAngle);
        Vector2 initialVelocity = new Vector2(randomVelX, randomVelY);
        pieceVelocities.add(initialVelocity);
    }
    private void spreadPieces(float delta) {
        alpha -= delta * durationTime;
        for (int i = 0; i < piecePositions.size; i++) {
            Vector2 piecePosition = piecePositions.get(i);
            piecePosition.add(pieceVelocities.get(i).x * delta, pieceVelocities.get(i).y * delta);
        }
    }
}
