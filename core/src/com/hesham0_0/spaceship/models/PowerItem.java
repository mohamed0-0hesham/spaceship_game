package com.hesham0_0.spaceship.models;

import static com.hesham0_0.spaceship.SpaceshipUtils.getItemImage;
import static com.hesham0_0.spaceship.SpaceshipUtils.getItemType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.hesham0_0.spaceship.PowerType;
import java.util.Random;

public class PowerItem {
    private Vector2 speed;
    private final Texture itemTexture;
    private final Sprite itemSprite;
    public static int itemRadius;
    public float x;
    public float y;
    public float angle;
    public PowerType itemType;
    private final Body itemBody;
    private boolean isAlive = true;
    public float rotationAngle = 0;

    public PowerItem(World world, float x, float y) {
        this.x = x;
        this.y = y;
        int randomItem = new Random().nextInt(3);
        this.itemType = getItemType(randomItem);
        itemTexture =  getItemImage(randomItem);

        itemRadius = itemTexture.getWidth() / 2;
        itemSprite = new Sprite(itemTexture);

        BodyDef rockBodyDef = new BodyDef();
        rockBodyDef.type = BodyDef.BodyType.DynamicBody;
        rockBodyDef.position.set(x, y);

        CircleShape shape = new CircleShape();
        shape.setRadius(itemRadius);

        FixtureDef rockFixture = new FixtureDef();
        rockFixture.shape = shape;
        rockFixture.density = 0.00005f;
        rockFixture.friction = 0;
        rockFixture.restitution = 0;
        itemBody = world.createBody(rockBodyDef);
        itemBody.createFixture(rockFixture);

        itemBody.setTransform(x, y, 0);
        MassData massData = new MassData();
        massData.mass = 0.00005f;
        massData.center.set(0.0f, 0.0f);
        massData.I = (0.15f);
        itemBody.setMassData(massData);

        itemBody.setUserData(this);
    }

    public void setSpeed(float magnitude, float angle) {
        this.angle = angle;
        float speedX = (float) Math.cos(angle) * magnitude;
        float speedY = (float) Math.sin(angle) * magnitude;
        speed = new Vector2(speedX, speedY);
    }

    public void update(float delta) {
        Vector2 distance = new Vector2(speed.x * delta, speed.y * delta);
        Vector2 position = itemBody.getPosition().add(distance);
        itemBody.setTransform(position, position.angleRad());
    }

    public void render(SpriteBatch batch) {
        itemSprite.setRotation(rotationAngle * MathUtils.radiansToDegrees);
        itemSprite.setPosition(
                itemBody.getPosition().x - itemSprite.getWidth() / 2f,
                itemBody.getPosition().y - itemSprite.getHeight() / 2f
        );
        itemSprite.draw(batch);

    }

    public void dispose() {
        itemTexture.dispose();
        itemBody.getWorld().destroyBody(itemBody);
    }

    public Body getBody() {
        return itemBody;
    }

    public void die() {
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }


}
