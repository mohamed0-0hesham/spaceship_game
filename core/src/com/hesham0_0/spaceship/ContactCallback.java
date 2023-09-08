package com.hesham0_0.spaceship;

import com.badlogic.gdx.math.Vector2;
import com.hesham0_0.spaceship.models.Bullet;
import com.hesham0_0.spaceship.models.Ring;
import com.hesham0_0.spaceship.models.Rock;
import com.hesham0_0.spaceship.models.Wall;

public interface ContactCallback {
    void bulletRockCollision(Rock rock, Bullet bullet, Vector2 contactPosition);

    void ringRocksCollision(Ring ring, Rock rock, Vector2 contactPosition);

    void wallRocksCollision(Wall wall, Rock rock);

    void wallBulletCollision(Wall wall, Bullet bullet);
}
