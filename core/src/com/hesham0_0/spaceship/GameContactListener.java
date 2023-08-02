package com.hesham0_0.spaceship;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.hesham0_0.spaceship.models.Bullet;
import com.hesham0_0.spaceship.models.Ring;
import com.hesham0_0.spaceship.models.Rock;
import com.hesham0_0.spaceship.models.Spaceship;
import com.hesham0_0.spaceship.models.Wall;

public class GameContactListener implements ContactListener {
    ContactCallback listener;
    public GameContactListener(ContactCallback listener) {
        this.listener=listener;
    }

    @Override
    public void beginContact(Contact contact) {
        Object bodyA = contact.getFixtureA().getBody().getUserData();
        Object bodyB = contact.getFixtureB().getBody().getUserData();
        Vector2 contactPosition =contact.getWorldManifold().getPoints()[0];
        if (bodyA instanceof Bullet && bodyB instanceof Rock) {
            Bullet bullet = (Bullet) bodyA;
            Rock rock = (Rock) bodyB;
            listener.bulletRockCollision(rock,bullet,contactPosition);

        } else if (bodyA instanceof Rock && bodyB instanceof Bullet) {
            Rock rock = (Rock) bodyA;
            Bullet bullet = (Bullet) bodyB;
            listener.bulletRockCollision(rock,bullet,contactPosition);
        }

        if (bodyA instanceof Spaceship && bodyB instanceof Rock) {
            Spaceship spaceship = (Spaceship) bodyA;
            Rock rock = (Rock) bodyB;
            listener.spaceshipRocksCollision(spaceship,rock,contactPosition);

        } else if (bodyA instanceof Rock && bodyB instanceof Spaceship) {
            Rock rock = (Rock) bodyA;
            Spaceship spaceship = (Spaceship) bodyB;
            listener.spaceshipRocksCollision(spaceship,rock, contactPosition);
        }

        if (bodyA instanceof Ring && bodyB instanceof Rock) {
            Ring ring = (Ring) bodyA;
            Rock rock = (Rock) bodyB;
            listener.ringRocksCollision(ring,rock,contactPosition);

        } else if (bodyA instanceof Rock && bodyB instanceof Ring) {
            Rock rock = (Rock) bodyA;
            Ring ring = (Ring) bodyB;
            listener.ringRocksCollision(ring,rock, contactPosition);
        }

        if (bodyA instanceof Wall && bodyB instanceof Rock) {
            Wall wall = (Wall) bodyA;
            Rock rock = (Rock) bodyB;
            listener.wallRocksCollision(wall,rock);

        } else if (bodyA instanceof Rock && bodyB instanceof Wall) {
            Rock rock = (Rock) bodyA;
            Wall wall = (Wall) bodyB;
            listener.wallRocksCollision(wall,rock);
        }

        if (bodyA instanceof Bullet && bodyB instanceof Wall) {
            Bullet bullet = (Bullet) bodyA;
            Wall wall = (Wall) bodyB;
            listener.wallBulletCollision(wall,bullet);

        } else if (bodyA instanceof Wall && bodyB instanceof Bullet) {
            Wall wall = (Wall) bodyA;
            Bullet bullet = (Bullet) bodyB;
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