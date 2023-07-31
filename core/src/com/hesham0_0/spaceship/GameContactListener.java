package com.hesham0_0.spaceship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.hesham0_0.spaceship.models.Bullet;
import com.hesham0_0.spaceship.models.Rock;
import com.hesham0_0.spaceship.models.Spaceship;

public class GameContactListener implements ContactListener {
    ContactCallback listener;
    public GameContactListener(ContactCallback listener) {
        this.listener=listener;
    }

    @Override
    public void beginContact(Contact contact) {
        // Get the bodies involved in the contact
        Object bodyA = contact.getFixtureA().getBody().getUserData();
        Object bodyB = contact.getFixtureB().getBody().getUserData();

        // Check if a bullet hit a rock
        if (bodyA instanceof Bullet && bodyB instanceof Rock) {
            Bullet bullet = (Bullet) bodyA;
            Rock rock = (Rock) bodyB;
            Gdx.app.log("BulletRockCollision","bullet-rock");
            listener.bulletRockCollision(rock,bullet);

        } else if (bodyA instanceof Rock && bodyB instanceof Bullet) {
            // If the order is reversed, call the method again with the bodies swapped
            Rock rock = (Rock) bodyA;
            Bullet bullet = (Bullet) bodyB;
            Gdx.app.log("BulletRockCollision","Rock-Bullet");
            listener.bulletRockCollision(rock,bullet);
        }
        if (bodyA instanceof Spaceship && bodyB instanceof Rock) {
            Spaceship spaceship = (Spaceship) bodyA;
            Rock rock = (Rock) bodyB;
            Gdx.app.log("BulletRockCollision","Spaceship-Bullet");
            listener.spaceshipRocksCollision(spaceship,rock);

        } else if (bodyA instanceof Rock && bodyB instanceof Spaceship) {

            // If the order is reversed, call the method again with the bodies swapped
            Rock rock = (Rock) bodyA;
            Spaceship spaceship = (Spaceship) bodyB;
            Gdx.app.log("BulletRockCollision","Spaceship-Bullet");
            listener.spaceshipRocksCollision(spaceship,rock);
        }
    }

    @Override
    public void endContact(Contact contact) {
        // Not used in this example
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Not used in this example
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Not used in this example
    }

}