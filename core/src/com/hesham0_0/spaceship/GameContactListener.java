package com.hesham0_0.spaceship;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.hesham0_0.spaceship.models.Bullet;
import com.hesham0_0.spaceship.models.Rock;

import java.util.List;

public class GameContactListener implements ContactListener {
    ContactCallback listener;
    public GameContactListener(ContactCallback listener) {
        this.listener=listener;
    }

    @Override
    public void beginContact(Contact contact) {
        // Get the bodies involved in the contact
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        // Check if a bullet hit a rock
        if (bodyA.getUserData() instanceof Bullet && bodyB.getUserData() instanceof Rock) {
            Bullet bullet = (Bullet) bodyA.getUserData();
            Rock rock = (Rock) bodyB.getUserData();
            lBulletRockCollision(rock,bullet);

        } else if (bodyA.getUserData() instanceof Rock && bodyB.getUserData() instanceof Bullet) {
            // If the order is reversed, call the method again with the bodies swapped
            Rock rock = (Rock) bodyA.getUserData();
            Bullet bullet = (Bullet) bodyB.getUserData();
            BulletRockCollision(rock,bullet);
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

    private void BulletRockCollision(Rock rock,Bullet bullet){
        // Remove the bullet and rock from their respective lists and from the world
        bullets.remove(bullet);
        rocks.remove(rock);
        world.destroyBody(bullet.getBody());
        world.destroyBody(rock.getBody());
    }
}