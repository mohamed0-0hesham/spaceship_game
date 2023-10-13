package com.hesham0_0.spaceship;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.hesham0_0.spaceship.models.Wall;
import com.hesham0_0.spaceship.models.spaceshipBullet;
import com.hesham0_0.spaceship.models.spaceshipRing;
import com.hesham0_0.spaceship.models.spaceshipRock;

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
        if (bodyA instanceof spaceshipBullet && bodyB instanceof spaceshipRock) {
            spaceshipBullet bullet = (spaceshipBullet) bodyA;
            spaceshipRock rock = (spaceshipRock) bodyB;
            listener.bulletRockCollision(rock,bullet,contactPosition);

        } else if (bodyA instanceof spaceshipRock && bodyB instanceof spaceshipBullet) {
            spaceshipRock rock = (spaceshipRock) bodyA;
            spaceshipBullet bullet = (spaceshipBullet) bodyB;
            listener.bulletRockCollision(rock,bullet,contactPosition);
        }


        if (bodyA instanceof spaceshipRing && bodyB instanceof spaceshipRock) {
            spaceshipRing ring = (spaceshipRing) bodyA;
            spaceshipRock rock = (spaceshipRock) bodyB;
            listener.ringRocksCollision(ring,rock,contactPosition);

        } else if (bodyA instanceof spaceshipRock && bodyB instanceof spaceshipRing) {
            spaceshipRock rock = (spaceshipRock) bodyA;
            spaceshipRing ring = (spaceshipRing) bodyB;
            listener.ringRocksCollision(ring,rock, contactPosition);
        }

        if (bodyA instanceof Wall && bodyB instanceof spaceshipRock) {
            Wall wall = (Wall) bodyA;
            spaceshipRock rock = (spaceshipRock) bodyB;
            listener.wallRocksCollision(wall,rock);

        } else if (bodyA instanceof spaceshipRock && bodyB instanceof Wall) {
            spaceshipRock rock = (spaceshipRock) bodyA;
            Wall wall = (Wall) bodyB;
            listener.wallRocksCollision(wall,rock);
        }

        if (bodyA instanceof spaceshipBullet && bodyB instanceof Wall) {
            spaceshipBullet bullet = (spaceshipBullet) bodyA;
            Wall wall = (Wall) bodyB;
            listener.wallBulletCollision(wall,bullet);

        } else if (bodyA instanceof Wall && bodyB instanceof spaceshipBullet) {
            Wall wall = (Wall) bodyA;
            spaceshipBullet bullet = (spaceshipBullet) bodyB;
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