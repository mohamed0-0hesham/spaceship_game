package com.hesham0_0.spaceship.models;

import static com.hesham0_0.spaceship.SpaceshipUtils.getRandomRockPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;

public class SpaceshipRock {
    private Vector2 speed;
    private final Texture rockTexture;
    private final Sprite rockSprite;
    public static int rockRadius;
    public float x;
    public float y;
    public float angle;
    public int level;
    public int health;
    private final Body rockBody;
    private boolean isAlive=true;
    public Color color ;
    public float rotationAngle = 0;
    public float rotationSpeed;

    public SpaceshipRock(World world, int level, float x, float y, Color color, float rotationSpeed) {
        this.x = x;
        this.y = y;
        this.level = level;
        this.health = level;
        this.color = color;
        this.rotationSpeed = rotationSpeed;

        rockTexture = new Texture(getRandomRockPath(level));
        rockRadius = rockTexture.getWidth() / 2;
        rockSprite = new Sprite(rockTexture);
        rockSprite.setColor(color);

        BodyDef rockBodyDef = new BodyDef();
        rockBodyDef.type = BodyDef.BodyType.DynamicBody;
        rockBodyDef.position.set(x, y);

        CircleShape shape = new CircleShape();
        shape.setRadius(rockRadius);

        FixtureDef rockFixture = new FixtureDef();
        rockFixture.shape = shape;
        rockFixture.density = 0.00005f;
        rockFixture.friction = 0;
        rockFixture.restitution = 0;
        rockBody = world.createBody(rockBodyDef);
        rockBody.createFixture(rockFixture);

        rockBody.setTransform(x, y, 0);
        MassData massData = new MassData();
        massData.mass = 0.00005f;
        massData.center.set(0.0f, 0.0f);
        massData.I = (0.15f);
        rockBody.setMassData(massData);

        rockBody.setUserData(this);
    }

    public void moveTo(Vector2 position, float speedMagnitude) {
        // Calculate the direction and distance to the target position
        float dx = position.x - x;
        float dy = position.y - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // Calculate the speed in the x and y directions
        float speedX = dx / distance * speedMagnitude;
        float speedY = dy / distance * speedMagnitude;

        angle= (float) Math.atan2(speedY, speedX);
        this.speed=new Vector2(speedX,speedY);
    }

    public void setSpeed(float magnitude, float angle) {
        this.angle= angle;
        float speedX = (float) Math.cos(angle) * magnitude;
        float speedY = (float) Math.sin(angle) * magnitude;
        speed=new Vector2(speedX,speedY);
    }
    public void update(float delta) {
        rotationAngle += delta * rotationSpeed;
        Vector2 distance = new Vector2(speed.x * delta, speed.y * delta);
        Vector2 position = rockBody.getPosition().add(distance);
        rockBody.setTransform(position, position.angleRad());
    }

    public void render(SpriteBatch batch) {
        rockSprite.setRotation(rotationAngle * MathUtils.radiansToDegrees);
        rockSprite.setPosition(
                rockBody.getPosition().x - rockSprite.getWidth() / 2f,
                rockBody.getPosition().y - rockSprite.getHeight() / 2f
        );
        rockSprite.draw(batch);

    }

    public void dispose() {
        // Dispose of the body when the bullet is no longer needed
        rockTexture.dispose();
        rockBody.getWorld().destroyBody(rockBody);
    }

    public Body getBody() {
        return rockBody;
    }

    public void die() {
        isAlive=false;
    }

    public void decreaseHealthBy(int bulletPower) {
        health = Math.max(0, health - bulletPower);
    }
    public boolean isAlive(){
        return isAlive;
    }

    public Polygon getRockPolygonShape() {
        Polygon polygon = new Polygon();
        int numVertices = 16;

        float[] vertices = new float[numVertices * 2];
        float angleIncrement = 2 * MathUtils.PI / numVertices;

        for (int i = 0; i < numVertices; i++) {
            float angle = i * angleIncrement;
            Vector2 vertex = new Vector2(rockTexture.getWidth() / 2f * MathUtils.cos(angle), rockTexture.getWidth() / 2f * MathUtils.sin(angle));
            vertices[i * 2] = vertex.x+rockBody.getPosition().x;
            vertices[i * 2 + 1] = vertex.y+rockBody.getPosition().y;
        }
        polygon.setVertices(vertices);
        return polygon;
    }
    public Texture getTexture(){
        return rockTexture;
    }
}
