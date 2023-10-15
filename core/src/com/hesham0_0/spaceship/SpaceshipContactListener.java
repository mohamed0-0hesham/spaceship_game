package com.hesham0_0.spaceship;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.hesham0_0.spaceship.models.PowerItem;
import com.hesham0_0.spaceship.models.Wall;
import com.hesham0_0.spaceship.models.SpaceshipBullet;
import com.hesham0_0.spaceship.models.SpaceshipRing;
import com.hesham0_0.spaceship.models.SpaceshipRock;

public class SpaceshipContactListener implements ContactListener {
    SpaceshipContactCallback listener;
    public SpaceshipContactListener(SpaceshipContactCallback listener) {
        this.listener=listener;
    }

    @Override
    public void beginContact(Contact contact) {
        Object bodyA = contact.getFixtureA().getBody().getUserData();
        Object bodyB = contact.getFixtureB().getBody().getUserData();
        Vector2 contactPosition =contact.getWorldManifold().getPoints()[0];
        if (bodyA instanceof SpaceshipBullet && bodyB instanceof SpaceshipRock) {
            SpaceshipBullet bullet = (SpaceshipBullet) bodyA;
            SpaceshipRock rock = (SpaceshipRock) bodyB;
            listener.bulletRockCollision(rock,bullet,contactPosition);

        } else if (bodyA instanceof SpaceshipRock && bodyB instanceof SpaceshipBullet) {
            SpaceshipRock rock = (SpaceshipRock) bodyA;
            SpaceshipBullet bullet = (SpaceshipBullet) bodyB;
            listener.bulletRockCollision(rock,bullet,contactPosition);
        }

        if (bodyA instanceof SpaceshipBullet && bodyB instanceof PowerItem) {
            SpaceshipBullet bullet = (SpaceshipBullet) bodyA;
            PowerItem item = (PowerItem) bodyB;
            listener.bulletItemCollision(item,bullet,contactPosition);

        } else if (bodyA instanceof PowerItem && bodyB instanceof SpaceshipBullet) {
            PowerItem item = (PowerItem) bodyA;
            SpaceshipBullet bullet = (SpaceshipBullet) bodyB;
            listener.bulletItemCollision(item,bullet,contactPosition);
        }

        if (bodyA instanceof SpaceshipRing && bodyB instanceof SpaceshipRock) {
            SpaceshipRing ring = (SpaceshipRing) bodyA;
            SpaceshipRock rock = (SpaceshipRock) bodyB;
            listener.ringRocksCollision(ring,rock,contactPosition);

        } else if (bodyA instanceof SpaceshipRock && bodyB instanceof SpaceshipRing) {
            SpaceshipRock rock = (SpaceshipRock) bodyA;
            SpaceshipRing ring = (SpaceshipRing) bodyB;
            listener.ringRocksCollision(ring,rock, contactPosition);
        }

        if (bodyA instanceof Wall && bodyB instanceof SpaceshipRock) {
            Wall wall = (Wall) bodyA;
            SpaceshipRock rock = (SpaceshipRock) bodyB;
            listener.wallRocksCollision(wall,rock);

        } else if (bodyA instanceof SpaceshipRock && bodyB instanceof Wall) {
            SpaceshipRock rock = (SpaceshipRock) bodyA;
            Wall wall = (Wall) bodyB;
            listener.wallRocksCollision(wall,rock);
        }

        if (bodyA instanceof Wall && bodyB instanceof PowerItem) {
            Wall wall = (Wall) bodyA;
            PowerItem item = (PowerItem) bodyB;
            listener.wallItemCollision(wall,item);

        } else if (bodyA instanceof PowerItem && bodyB instanceof Wall) {
            PowerItem item = (PowerItem) bodyA;
            Wall wall = (Wall) bodyB;
            listener.wallItemCollision(wall,item);
        }

        if (bodyA instanceof SpaceshipBullet && bodyB instanceof Wall) {
            SpaceshipBullet bullet = (SpaceshipBullet) bodyA;
            Wall wall = (Wall) bodyB;
            listener.wallBulletCollision(wall,bullet);

        } else if (bodyA instanceof Wall && bodyB instanceof SpaceshipBullet) {
            Wall wall = (Wall) bodyA;
            SpaceshipBullet bullet = (SpaceshipBullet) bodyB;
            listener.wallBulletCollision(wall,bullet);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}