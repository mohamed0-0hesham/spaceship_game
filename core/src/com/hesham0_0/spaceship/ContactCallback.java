package com.hesham0_0.spaceship;

import com.hesham0_0.spaceship.models.Bullet;
import com.hesham0_0.spaceship.models.Rock;
import com.hesham0_0.spaceship.models.Spaceship;

public interface ContactCallback {
    void bulletRockCollision(Rock rock, Bullet bullet);
    void spaceshipRocksCollision(Spaceship spaceship, Rock rock);
}
