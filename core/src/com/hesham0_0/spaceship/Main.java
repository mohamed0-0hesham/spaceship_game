package com.hesham0_0.spaceship;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.hesham0_0.spaceship.models.Bullet;
import com.hesham0_0.spaceship.models.Explosion;
import com.hesham0_0.spaceship.models.Ring;
import com.hesham0_0.spaceship.models.Rock;
import com.hesham0_0.spaceship.models.Spaceship;
import com.hesham0_0.spaceship.models.Wall;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Main extends ApplicationAdapter implements InputProcessor {
	private OrthographicCamera camera;
	private final float virtualWidth = 720;
	private final float virtualHeight = 1280;
	private SpriteBatch batch;
	private World world;
	private Spaceship spaceship;
	private List<Bullet> bullets;
	private float bulletSpeed = 500;
	private float rockSpeed =100;
	private float delta;
	private Box2DDebugRenderer debugRenderer;
	public static float RGB_COLOR_COEFFICIENT= 1/256f;
	public static float RING_1_START_LEVEL= 150;
	public static float RING_2_START_LEVEL= 330;
	public static float RING_3_START_LEVEL= 670;
	private List<Rock> rocks = new ArrayList<>();
	private Ring ring_1;
	private Ring ring_2;
	private Ring ring_3;
	private List<Explosion> explosions = new ArrayList<>();
	private List<Ring> rings= new ArrayList<>();
	private List<Wall> walls= new ArrayList<>();
	private long rockInterval = 4000;
	private Rock parentRock=null;
	private int rockCounter=0;
	private Texture rockTexture;


	@Override
	public void create() {
		Box2D.init();
		batch = new SpriteBatch();
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();

		camera = new OrthographicCamera(virtualWidth, virtualHeight);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		Viewport viewport = new FitViewport(virtualWidth, virtualHeight, camera);
		viewport.apply();
		batch.setProjectionMatrix(camera.combined);
		Gdx.input.setInputProcessor(this);

		spaceship = new Spaceship(world, virtualWidth, virtualHeight);
		rockTexture =new Texture("rock3.png");
		createWalls();
		bullets = new ArrayList<>();

		Timer rockTimer = new Timer();
		rockTimer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				createRocks();
			}
		}, 0, rockInterval / 1000f);
		setCollisionListener();
	}

	private void createWalls() {
		float bodyThickness=100f;
		float distanceOutScreen =rockTexture.getWidth()*2f;

		Vector2 leftWallPos = new Vector2(-distanceOutScreen, virtualHeight/2);
		Vector2 rightWallPos = new Vector2(virtualWidth+ distanceOutScreen, virtualHeight/2f);
		Vector2 topWallPos = new Vector2(virtualWidth/2f, virtualHeight+distanceOutScreen);
		Vector2 bottomWallPos = new Vector2(virtualWidth/2f, -distanceOutScreen);


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
		GameContactListener contactListener = new GameContactListener(new ContactCallback() {
			@Override
			public void bulletRockCollision(Rock rock, Bullet bullet, Vector2 contactPosition) {
				parentRock=rock;
				bullet.die();
				createExplosion(contactPosition);

			}

			@Override
			public void spaceshipRocksCollision(Spaceship spaceship, Rock rock, Vector2 contactPosition) {
				createExplosion(contactPosition);
			}

			@Override
			public void ringRocksCollision(Ring ring, Rock rock, Vector2 contactPosition) {
				parentRock=rock;
				createExplosion(contactPosition);
			}

			@Override
			public void wallRocksCollision(Wall wall, Rock rock) {
				rock.die();
			}

			@Override
			public void wallBulletCollision(Wall wall, Bullet bullet) {
				bullet.die();
			}
		});
		world.setContactListener(contactListener);
	}

	@Override
	public void render() {
		delta = Gdx.graphics.getDeltaTime();
		world.step(1 / 60f, 6, 2);
		spaceship.update(delta);
		updateBullets(delta);
		updateRock(delta);
		updateExplosion(delta);
		updateRing(delta);
		ScreenUtils.clear(36 * RGB_COLOR_COEFFICIENT, 49 * RGB_COLOR_COEFFICIENT, 66 * RGB_COLOR_COEFFICIENT, 1);
		batch.begin();
		for (Bullet bullet : bullets) {
			bullet.render(batch);
		}
		batch.end();

		batch.begin();
		for (Rock rock : rocks) {
			rock.render(batch);
		}
		batch.end();

		batch.begin();
		for (Explosion explosion : explosions) {
			explosion.render(batch);
		}
		batch.end();

		batch.begin();
		for (Ring ring : rings) {
			ring.render(batch);
		}
		batch.end();

		batch.begin();
		spaceship.render(batch);
		batch.end();
	}

	private void updateBullets(float delta) {
		Iterator<Bullet> bulletIterator = bullets.iterator();
		while (bulletIterator.hasNext()) {
			Bullet bullet = bulletIterator.next();
			bullet.update();
			if (!bullet.isAlive()) {
				bulletIterator.remove();
				world.destroyBody(bullet.getBody());
			}
		}
	}

	private void updateRock(float delta) {
		if (parentRock!=null){
			createChildRocks(parentRock);
			parentRock.die();
			parentRock=null;
		}

		Iterator<Rock> rockIterator = rocks.iterator();
		while (rockIterator.hasNext()) {
			Rock rock = rockIterator.next();
			rock.update(delta);
			if (!rock.isAlive()) {
				rockIterator.remove();
				world.destroyBody(rock.getBody());
			}
		}
	}

	private void updateExplosion(float delta) {
		Iterator<Explosion> explosionIterator = explosions.iterator();
		while (explosionIterator.hasNext()) {
			Explosion explosion = explosionIterator.next();
			explosion.update(delta);
			if (!explosion.isAlive()) {
				explosionIterator.remove();
			}
		}
	}

	private void updateRing(float delta) {
		Iterator<Ring> ringIterator = rings.iterator();
		while (ringIterator.hasNext()) {
			Ring ring = ringIterator.next();
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
		for (Bullet bullet : bullets) {
			bullet.dispose();
		}
		for (Rock rock : rocks) {
			rock.dispose();
		}
		for (Ring ring : rings) {
			ring.dispose();
		}
		for (Wall wall : walls) {
			wall.dispose();
		}
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

		float angle = MathUtils.atan2(worldY - spaceship.getBody().getPosition().y , worldX - spaceship.getBody().getPosition().x );

		spaceship.setTargetAngle(angle);

		createBullets(worldX, worldY);
		createRings(screenY);
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

	private void createBullets(float worldX, float worldY) {

		float directionX = worldX - spaceship.getBody().getPosition().x;
		float directionY = worldY - spaceship.getBody().getPosition().y;

		float distance = bulletSpeed * delta;
		float length = (float) Math.sqrt(directionX * directionX + directionY * directionY);
		float forceX = directionX / length * distance;
		float forceY = directionY / length * distance;

		Bullet bullet = new Bullet(world,
				spaceship.getBody().getPosition().x ,
				spaceship.getBody().getPosition().y ,
				10, 10);

		bullet.setSpeed(forceX, forceY);
		bullets.add(bullet);
	}

	private void createRocks() {
		Random random = new Random();
		int edge = random.nextInt(4); // generate a random edge index from 0 to 3
		float x, y, angle;
		switch (edge) {
			// top edge
			// random angle between -90 and 90 degrees
			case 1: // right edge
				x = camera.viewportWidth;
				y = random.nextFloat() * camera.viewportHeight;
				angle = 135 + random.nextFloat() * 225; // random angle between 180-+45 degrees
				break;
			case 2: // bottom edge
				x = random.nextFloat() * camera.viewportWidth;
				y = 0;
				angle = 45 + random.nextFloat() * 135; // random angle between 90-+45 degrees
				break;
			case 3: // left edge
				x = 0;
				y = random.nextFloat() * camera.viewportHeight;
				angle = -45 + random.nextFloat() * 45; // random angle between 0-+45 and 180 degrees
				break;
			default: // fallback to top edge
				x = random.nextFloat() * camera.viewportWidth;
				y = camera.viewportHeight;
				angle = -135 + random.nextFloat() * -45; // random angle between 0-+(-90) and 180 degrees
				break;
		}
		int randomLevel = random.nextInt(3) + 1;
		Rock rock = new Rock(world, randomLevel, x, y);
		rocks.add(rock);

		if(rockCounter%3==0){
			rock.setSpeed(rockSpeed, (float) Math.toRadians(angle));
		}else {
			rock.moveTo(spaceship.getBody().getPosition(),rockSpeed);
		}
		rockCounter++;
	}
	private void createChildRocks(Rock parentRock) {
		if (parentRock.level==1){return;}
		int levelRock1=1;
		int levelRock2 = parentRock.level == 3 ? 2 : 1;
		Rock rock1 = new Rock(world, levelRock1, parentRock.getBody().getPosition().x, parentRock.getBody().getPosition().y);
		rock1.setSpeed(rockSpeed, parentRock.angle+(float) Math.toRadians(45));
		Rock rock2 = new Rock(world, levelRock2, parentRock.getBody().getPosition().x, parentRock.getBody().getPosition().y);
		rock2.setSpeed(rockSpeed, parentRock.angle+(float) Math.toRadians(-45));
		rocks.add(rock1);
		rocks.add(rock2);
	}

	private void createExplosion(Vector2 contactPosition) {
		Explosion explosion = new Explosion(contactPosition);
		explosions.add(explosion);
	}

	private void createRings(float pressureLevel) {
		int pressure = (int) Math.min(pressureLevel, 1000);
		createOrDestroyRing(pressure, RING_1_START_LEVEL, spaceship.getTexture().getWidth() * 0.8f, ring_1, 0);
		createOrDestroyRing(pressure, RING_2_START_LEVEL, spaceship.getTexture().getWidth() * 1.1f, ring_2, 1);
		createOrDestroyRing(pressure, RING_3_START_LEVEL, spaceship.getTexture().getWidth() * 1.4f, ring_3, 2);
	}

	private void createOrDestroyRing(float pressure, float startLevel, float width, Ring ring, int index) {
		if (pressure > startLevel) {
			if (ring == null || !ring.isAlive()) {
				ring = new Ring(world, spaceship, pressure - startLevel, width, index);
				rings.add(ring);
			} else {
				ring.update(pressure - startLevel);
			}
		} else {
			if (ring != null && ring.isAlive()) {
				ring.die();
			}
		}

		switch (index) {
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

}
