package com.hesham0_0.spaceship;

import com.badlogic.gdx.math.Vector2;
import com.hesham0_0.spaceship.models.PowerItem;
import com.hesham0_0.spaceship.models.Wall;
import com.hesham0_0.spaceship.models.SpaceshipBullet;
import com.hesham0_0.spaceship.models.SpaceshipRing;
import com.hesham0_0.spaceship.models.SpaceshipRock;

public interface SpaceshipContactCallback {
    void bulletRockCollision(SpaceshipRock rock, SpaceshipBullet bullet, Vector2 contactPosition);

    void ringRocksCollision(SpaceshipRing ring, SpaceshipRock rock, Vector2 contactPosition);

    void bulletItemCollision(PowerItem item, SpaceshipBullet bullet, Vector2 contactPosition);

    void wallRocksCollision(Wall wall, SpaceshipRock rock);

    void wallBulletCollision(Wall wall, SpaceshipBullet bullet);

    void wallItemCollision(Wall wall, PowerItem item);
}
