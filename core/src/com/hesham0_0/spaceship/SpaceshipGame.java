package com.hesham0_0.spaceship;

import static com.hesham0_0.spaceship.SpaceshipUtils.MAX_PRESSURE;
import static com.hesham0_0.spaceship.SpaceshipUtils.MILLIS_IN_SECOND;
import static com.hesham0_0.spaceship.SpaceshipUtils.RGB_COLOR_COEFFICIENT;
import static com.hesham0_0.spaceship.SpaceshipUtils.RING_START_LEVEL;
import static com.hesham0_0.spaceship.SpaceshipUtils.bulletSpeed;
import static com.hesham0_0.spaceship.SpaceshipUtils.explosionAnimationTime;
import static com.hesham0_0.spaceship.SpaceshipUtils.getRandomRockColor;
import static com.hesham0_0.spaceship.SpaceshipUtils.getRandomRockRotation;
import static com.hesham0_0.spaceship.SpaceshipUtils.rockSpeed;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hesham0_0.spaceship.models.PowerItem;
import com.hesham0_0.spaceship.models.RockFragments;
import com.hesham0_0.spaceship.models.Spaceship;
import com.hesham0_0.spaceship.models.SpaceshipExplosion;
import com.hesham0_0.spaceship.models.Wall;
import com.hesham0_0.spaceship.models.SpaceshipBullet;
import com.hesham0_0.spaceship.models.SpaceshipRing;
import com.hesham0_0.spaceship.models.SpaceshipRock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class SpaceshipGame extends ApplicationAdapter implements InputProcessor {
    private OrthographicCamera camera;
    private final float virtualWidth = 720;
    private float virtualHeight = 1280;
    float aspectRation = 1;
    private SpriteBatch batch;
    private World world;
    private Spaceship spaceship;
    private List<SpaceshipBullet> bullets;
    private Box2DDebugRenderer debugRenderer;
    private final List<SpaceshipRock> rocks = new ArrayList<>();
    private SpaceshipRing ring_1;
    private SpaceshipRing ring_2;
    private SpaceshipRing ring_3;
    private final List<SpaceshipExplosion> explosions = new ArrayList<>();
    private final List<RockFragments> rockFragments = new ArrayList<>();
    private final List<SpaceshipRing> rings = new ArrayList<>();
    private final List<Wall> walls = new ArrayList<>();
    private float rockSpeedDifficultyCoefficient = 0.5f;
    private long rockInterval = 3;
    private int rockCounter = 1;
    private static final float BLINKS_TIME = 2;
    private Texture rockTexture;
    private Texture backgroundTexture;
    PointsUpdateListener pointsUpdateListener;
    private int points = 0;
    private ShapeRenderer shapeRenderer;
    private boolean isGamePaused = false;
    private final List<SpaceshipRock> collisionRocks = new ArrayList<>();
    private final List<SpaceshipRock> collisionRingRocks = new ArrayList<>();
    private final List<PowerItem> powerItems = new ArrayList<>();
    private long lastShootingTime = 0;
    private int rockLevels = 1;
    public static float bulletsFrequency = 3.0f;
    public boolean forceField = true;
    float ringPower = 0;
    float ringCounter = -1;

    public SpaceshipGame(PointsUpdateListener pointsUpdateListener) {
        this.pointsUpdateListener = pointsUpdateListener;
    }

    @Override
    public void create() {
        Box2D.init();
        batch = new SpriteBatch();
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();
        aspectRation = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        virtualHeight = virtualWidth / aspectRation;

        camera = new OrthographicCamera(virtualWidth, virtualHeight);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        Viewport viewport = new FitViewport(virtualWidth, virtualHeight, camera);
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        Gdx.input.setInputProcessor(this);

        spaceship = new Spaceship(world, virtualWidth, virtualHeight);
        backgroundTexture = new Texture("spaceshipGame/space_background.png");
        rockTexture = new Texture("spaceshipGame/rocks_level_3/1.png");
        createWalls();
        bullets = new ArrayList<>();

        Timer rockTimer = new Timer();
        rockTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (!isGamePaused) {
                    createRocks();
                }
            }
        }, 0, rockInterval);

        Timer itemTimer = new Timer();
        itemTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (!isGamePaused) {
                    createItems();
                }
            }
        }, 0, rockInterval * 4);

        setCollisionListener();
        shapeRenderer = new ShapeRenderer();
        setGameSetting();
    }

    private void bulletTime(float angle) {
        float bulletInterval = (MILLIS_IN_SECOND / bulletsFrequency);
        if ((System.currentTimeMillis() - lastShootingTime) >= bulletInterval && spaceship.state != SpaceshipState.DESTROYED) {
            createBullets(angle);
            lastShootingTime = System.currentTimeMillis();
        }
    }


    private void createWalls() {
        float bodyThickness = 100f;
        float distanceOutScreen = rockTexture.getWidth() * 2f;

        Vector2 leftWallPos = new Vector2(-distanceOutScreen, virtualHeight / 2);
        Vector2 rightWallPos = new Vector2(virtualWidth + distanceOutScreen, virtualHeight / 2f);
        Vector2 topWallPos = new Vector2(virtualWidth / 2f, virtualHeight + distanceOutScreen);
        Vector2 bottomWallPos = new Vector2(virtualWidth / 2f, -distanceOutScreen);


        Wall leftWall = new Wall(world, leftWallPos, virtualHeight, bodyThickness, "leftWall");
        walls.add(leftWall);

        Wall rightWall = new Wall(world, rightWallPos, virtualHeight, bodyThickness, "rightWall");
        walls.add(rightWall);

        Wall topWall = new Wall(world, topWallPos, bodyThickness, virtualWidth, "topWall");
        walls.add(topWall);

        Wall bottomWall = new Wall(world, bottomWallPos, bodyThickness, virtualWidth, "bottomWall");
        walls.add(bottomWall);
    }

    private void setCollisionListener() {
        SpaceshipContactListener contactListener = new SpaceshipContactListener(new SpaceshipContactCallback() {
            @Override
            public void bulletRockCollision(SpaceshipRock rock, SpaceshipBullet bullet, Vector2 contactPosition) {
                if (!collisionRingRocks.contains(rock)) {
                    rock.decreaseHealthBy(bullet.getBulletPower());
                    if (rock.health == 0) {
                        collisionRingRocks.add(rock);
                        createRockFragments(rock, "+", rock.level);
                        points = points + rock.level;
                        setGameSetting();
                    } else {
                        createExplosion(contactPosition, 0);
                    }
                    bullet.die();
                }
            }


            @Override
            public void ringRocksCollision(SpaceshipRing ring, SpaceshipRock rock, Vector2 contactPosition) {
                if (forceField && !collisionRingRocks.contains(rock)) {
                    collisionRingRocks.add(rock);
                    createExplosion(contactPosition, rock.level);
                    points = points + rock.level;
                    setGameSetting();
                }
            }

            @Override
            public void bulletItemCollision(PowerItem item, SpaceshipBullet bullet, Vector2 contactPosition) {
                item.die();
                switch (item.itemType) {
                    case RINGS:
                        runTheRingsPower();
                        break;
                    case BULLETS:
                        bulletsFrequency++;
                        break;
                    case HEALTH:
                        pointsUpdateListener.onHealthChanges(+5);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void wallRocksCollision(Wall wall, SpaceshipRock rock) {
                rock.die();
            }

            @Override
            public void wallBulletCollision(Wall wall, SpaceshipBullet bullet) {
                bullet.die();
            }

            @Override
            public void wallItemCollision(Wall wall, PowerItem item) {
                item.die();
            }
        });
        world.setContactListener(contactListener);
    }

    public void runTheRingsPower() {
        ringCounter = -1;

        Timer itemTimer = new Timer();
        itemTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (!isGamePaused) {
                    ringCounter++;
                    ringPower = (float) (MAX_PRESSURE * Math.sin(Math.toRadians(ringCounter)));
                    createRings(ringPower);
                    Gdx.app.log("ringPower", "ringCounter = " + ringCounter);
                    Gdx.app.log("ringPower", "ringPower = " + ringPower);
                }
            }
        }, 0, 0.1f, 180);

    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        world.step(1 / 60f, 6, 2);
        if (!isGamePaused) {
            spaceship.update(delta);
            updateBullets(delta);
            updateRock(delta);
            updateItems(delta);
            updateExplosion(delta);
            updateRocksFragments(delta);
            updateRing();
        }
        ScreenUtils.clear(36 * RGB_COLOR_COEFFICIENT, 49 * RGB_COLOR_COEFFICIENT, 66 * RGB_COLOR_COEFFICIENT, 1);
        batch.begin();
        batch.draw(backgroundTexture,
                virtualWidth / 2 - backgroundTexture.getWidth() / 2f,
                virtualHeight / 2 - backgroundTexture.getHeight() / 2f
        );
        batch.end();

        batch.begin();
        for (SpaceshipBullet bullet : bullets) {
            bullet.render(batch);
        }
        batch.end();

        batch.begin();
        for (SpaceshipRock rock : rocks) {
            rock.render(batch);
        }
        batch.end();

        batch.begin();
        for (PowerItem item : powerItems) {
            item.render(batch);
        }
        batch.end();

        batch.begin();
        spaceship.render(batch);
        batch.end();

        batch.begin();
        for (SpaceshipExplosion explosion : explosions) {
            explosion.render(batch);
        }
        batch.end();

        batch.begin();
        for (RockFragments fragments : rockFragments) {
            fragments.render(batch);
        }
        batch.end();

        batch.begin();
        for (SpaceshipRing ring : rings) {
            ring.render(batch);
        }
        batch.end();

//		debugRenderer.render(world, camera.combined);
    }

    private void updateBullets(float delta) {
        Iterator<SpaceshipBullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            SpaceshipBullet bullet = bulletIterator.next();
            bullet.update(delta);
            if (!bullet.isAlive()) {
                bulletIterator.remove();
                world.destroyBody(bullet.getBody());
            }
        }
    }

    private void updateRock(float delta) {
        Iterator<SpaceshipRock> rockCollisionIterator = collisionRingRocks.iterator();
        while (rockCollisionIterator.hasNext()) {
            SpaceshipRock rock = rockCollisionIterator.next();
            createChildRocks(rock);
            rock.die();
            rockCollisionIterator.remove();
        }

        Iterator<SpaceshipRock> rockIterator = rocks.iterator();
        while (rockIterator.hasNext()) {
            SpaceshipRock rock = rockIterator.next();

            if (Intersector.overlapConvexPolygons(spaceship.getSensorShape(), rock.getRockPolygonShape()) && !collisionRocks.contains(rock)) {
                if (spaceship.state != SpaceshipState.DESTROYED){
                    pointsUpdateListener.onHealthChanges(-rock.level);
                }
                if (points == 0 && spaceship.state != SpaceshipState.DESTROYED) {
                    spaceship.createPieces(rock.angle);
                    spaceship.state = SpaceshipState.DESTROYED;
                    rock.die();
                    createRockFragments(rock, "-", rock.level);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            spaceship.state = SpaceshipState.STABLE;
                            spaceship.resetPiecesAlpha();
                        }
                    }, 2);
                } else if (points != 0) {
                    points = Math.max(points - rock.level, 0);
                    rock.die();
                    createRockFragments(rock, "-", rock.level);
                    if (spaceship.state != SpaceshipState.BLINKS) {
                        spaceship.state = SpaceshipState.BLINKS;
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                spaceship.state = SpaceshipState.STABLE;
                            }
                        }, BLINKS_TIME);
                    }
                }
                setGameSetting();
            }
            rock.update(delta);
            if (!rock.isAlive()) {
                rockIterator.remove();
                collisionRocks.remove(rock);
                world.destroyBody(rock.getBody());
            }
        }
    }

    private void updateItems(float delta) {
        Iterator<PowerItem> powerItem = powerItems.iterator();
        while (powerItem.hasNext()) {
            PowerItem item = powerItem.next();
            item.update(delta);
            if (!item.isAlive()) {
                powerItem.remove();
                world.destroyBody(item.getBody());
            }
        }
    }

    private void updateExplosion(float delta) {
        Iterator<SpaceshipExplosion> explosionIterator = explosions.iterator();
        while (explosionIterator.hasNext()) {
            SpaceshipExplosion explosion = explosionIterator.next();
            explosion.update(delta);
            if (!explosion.isAlive()) {
                explosionIterator.remove();
            }
        }
    }

    private void updateRocksFragments(float delta) {
        Iterator<RockFragments> fragmentsIterator = rockFragments.iterator();
        while (fragmentsIterator.hasNext()) {
            RockFragments fragments = fragmentsIterator.next();
            fragments.update(delta);
            if (!fragments.isAlive()) {
                fragmentsIterator.remove();
            }
        }
    }

    private void updateRing() {
        Iterator<SpaceshipRing> ringIterator = rings.iterator();
        while (ringIterator.hasNext()) {
            SpaceshipRing ring = ringIterator.next();
            if (!ring.isAlive()) {
                ringIterator.remove();
                world.destroyBody(ring.getBody());
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // Update the viewport when the screen is resized
        camera.viewportWidth = virtualWidth;
        camera.viewportHeight = virtualHeight;
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        Viewport viewport = new FitViewport(virtualWidth, virtualHeight, camera);
        viewport.update(width, height);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void dispose() {
        batch.dispose();
        spaceship.dispose();
        for (SpaceshipBullet bullet : bullets) {
            bullet.dispose();
        }
        for (SpaceshipRock rock : rocks) {
            rock.dispose();
        }
        for (PowerItem item : powerItems) {
            item.dispose();
        }
        for (SpaceshipRing ring : rings) {
            ring.dispose();
        }
        for (Wall wall : walls) {
            wall.dispose();
        }
        for (SpaceshipExplosion explosion : explosions) {
            explosion.dispose();
        }
        for (RockFragments fragments : rockFragments) {
            fragments.dispose();
        }
        shapeRenderer.dispose();
        world.dispose();
        debugRenderer.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Convert screen coordinates to world coordinates
        Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY, 0));
        float worldX = worldCoordinates.x;
        float worldY = worldCoordinates.y;
        float angle = MathUtils.atan2(worldY - spaceship.getBody().getPosition().y, worldX - spaceship.getBody().getPosition().x);

        spaceship.setTargetAngle(angle);
        bulletTime(angle);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer == 0) {
            Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY, 0));
            float worldX = worldCoordinates.x;
            float worldY = worldCoordinates.y;
            float angle = MathUtils.atan2(worldY - spaceship.getBody().getPosition().y, worldX - spaceship.getBody().getPosition().x);
            spaceship.setDraggingAngle(angle);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    private void createBullets(float angle) {
        SpaceshipBullet bullet = new SpaceshipBullet(world,
                spaceship.getBody().getPosition().x,
                spaceship.getBody().getPosition().y,
                10, 10, spaceship.getAngle(),
                1
        );

        bullet.setSpeed(angle, bulletSpeed);
        bullets.add(bullet);
    }

    private void createRocks() {
        float halfRockWidth = rockTexture.getWidth() / 2f;
        float[] position = getRandomEdge(halfRockWidth);
        Random random = new Random();
        int randomLevel = random.nextInt(rockLevels) + 1;
        SpaceshipRock rock = new SpaceshipRock(world, randomLevel, position[0], position[1], getRandomRockColor(), getRandomRockRotation());
        rocks.add(rock);

        if (rockCounter % 3 == 0) {
            rock.setSpeed(rockSpeed * rockSpeedDifficultyCoefficient, (float) Math.toRadians(position[2]));
        } else {
            rock.moveTo(spaceship.getBody().getPosition(), rockSpeed * rockSpeedDifficultyCoefficient);
        }
        rockCounter++;
    }

    private void createItems() {
        float halfRockWidth = rockTexture.getWidth() / 2f;
        float[] position = getRandomEdge(halfRockWidth);
        PowerItem item = new PowerItem(world, position[0], position[1]);
        powerItems.add(item);
        item.setSpeed(rockSpeed * rockSpeedDifficultyCoefficient, (float) Math.toRadians(position[2]));

    }

    public float[] getRandomEdge(float halfWidth) {
        Random random = new Random();
        int edge = random.nextInt(4); // generate a random edge index from 0 to 3
        float x, y, angle;
        switch (edge) {
            // top edge
            // random angle between -90 and 90 degrees
            case 1: // right edge
                x = camera.viewportWidth + halfWidth;
                y = random.nextFloat() * camera.viewportHeight;
                angle = 135 + random.nextFloat() * 225; // random angle between 180-+45 degrees
                break;
            case 2: // bottom edge
                x = random.nextFloat() * camera.viewportWidth;
                y = 0 - halfWidth;
                angle = 45 + random.nextFloat() * 135; // random angle between 90-+45 degrees
                break;
            case 3: // left edge
                x = 0 - halfWidth;
                y = random.nextFloat() * camera.viewportHeight;
                angle = -45 + random.nextFloat() * 45; // random angle between 0-+45 and 180 degrees
                break;
            default: // fallback to top edge
                x = random.nextFloat() * camera.viewportWidth;
                y = camera.viewportHeight + halfWidth;
                angle = -135 + random.nextFloat() * -45; // random angle between 0-+(-90) and 180 degrees
                break;
        }
        return new float[]{x, y, angle};
    }

    private void createChildRocks(SpaceshipRock parentRock) {
        if (parentRock.level == 1) {
            return;
        }
        int levelRock1 = 1;
        int levelRock2 = parentRock.level == 3 ? 2 : 1;
        SpaceshipRock rock1 = new SpaceshipRock(world, levelRock1, parentRock.getBody().getPosition().x, parentRock.getBody().getPosition().y, parentRock.color, parentRock.rotationSpeed);
        rock1.setSpeed(rockSpeed * rockSpeedDifficultyCoefficient, parentRock.angle + (float) Math.toRadians(45));
        SpaceshipRock rock2 = new SpaceshipRock(world, levelRock2, parentRock.getBody().getPosition().x, parentRock.getBody().getPosition().y, parentRock.color, parentRock.rotationSpeed);
        rock2.setSpeed(rockSpeed * rockSpeedDifficultyCoefficient, parentRock.angle + (float) Math.toRadians(-45));
        rocks.add(rock1);
        rocks.add(rock2);
    }

    private void createExplosion(Vector2 contactPosition, int score) {
        SpaceshipExplosion explosion = new SpaceshipExplosion(contactPosition, explosionAnimationTime, "+", score);
        explosions.add(explosion);
    }

    private void createRockFragments(SpaceshipRock rock, String signal, int score) {
        RockFragments fragments = new RockFragments(explosionAnimationTime, signal, score, rock);
        rockFragments.add(fragments);
    }

    private void createRings(float ringPower) {
        createOrDestroyRing(ringPower, spaceship.getTexture().getWidth() * 0.8f, ring_1, 0);
        createOrDestroyRing(ringPower, spaceship.getTexture().getWidth() * 1.1f, ring_2, 1);
        createOrDestroyRing(ringPower, spaceship.getTexture().getWidth() * 1.4f, ring_3, 2);
    }

    private void createOrDestroyRing(float pressure, float width, SpaceshipRing ring, int level) {
        if (pressure > RING_START_LEVEL[level]) {
            if (ring == null || !ring.isAlive()) {
                ring = new SpaceshipRing(world, spaceship.getBody().getPosition(), width, shapeRenderer, level, forceField);
                rings.add(ring);
            } else {
                ring.update(pressure);
            }
        } else {
            if (ring != null && ring.isAlive()) {
                ring.die();
            }
        }

        switch (level) {
            case 0:
                ring_1 = ring;
                break;
            case 1:
                ring_2 = ring;
                break;
            case 2:
                ring_3 = ring;
                break;
        }
    }


    public void alterGameStatus(boolean isPaused) {
        this.isGamePaused = isPaused;
    }

    public void setGameSetting() {
        pointsUpdateListener.onPointsUpdated(points);
        if (points >= 0 && points <= 5) {
            setDifficultyValues(1, 1f, 0.5f);
        } else if (points > 6 && points <= 15) {
            setDifficultyValues(2, 1f, 0.3f);
        } else if (points > 15 && points <= 30) {
            setDifficultyValues(3, 1f, 0.3f);
        } else if (points > 31 && points <= 40) {
            setDifficultyValues(3, 1f, 0.3f);
        } else if (points > 41 && points <= 55) {
            setDifficultyValues(3, 1f, 0.2f);
        } else if (points > 56 && points <= 70) {
            setDifficultyValues(3, 1.25f, 0.2f);
        } else if (points > 71) {
            setDifficultyValues(3, 1.5f, 0.1f);
        }
    }

    public void setDifficultyValues(int levels, float speed, float interval) {
        rockLevels = levels;
        rockSpeedDifficultyCoefficient = speed;
        rockInterval = (long) interval;
    }
}
