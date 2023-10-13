package com.hesham0_0.spaceship;

import com.badlogic.gdx.math.Vector2;
import com.hesham0_0.spaceship.models.Wall;
import com.hesham0_0.spaceship.models.spaceshipBullet;
import com.hesham0_0.spaceship.models.spaceshipRing;
import com.hesham0_0.spaceship.models.spaceshipRock;

public interface SpaceshipContactCallback {
    void bulletRockCollision(spaceshipRock rock, spaceshipBullet bullet, Vector2 contactPosition);

    void ringRocksCollision(spaceshipRing ring, spaceshipRock rock, Vector2 contactPosition);

    void wallRocksCollision(Wall wall, spaceshipRock rock);

    void wallBulletCollision(Wall wall, spaceshipBullet bullet);
}
