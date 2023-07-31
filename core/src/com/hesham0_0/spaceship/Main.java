package com.hesham0_0.spaceship;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.hesham0_0.spaceship.models.Rock;
import com.hesham0_0.spaceship.models.Spaceship;
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
	 List<Bullet> bullets;
	private float bulletSpeed = 500;
	private float rockSpeed =100;
	private float delta;
	private Box2DDebugRenderer debugRenderer;
	private ShapeRenderer shapeRenderer;
	List<Rock> rocks = new ArrayList<>();
	long rockInterval = 1000;

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
		bullets = new ArrayList<>();
		shapeRenderer = new ShapeRenderer();

		Timer rockTimer = new Timer();
		rockTimer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				createRocks();
			}
		}, 0, rockInterval / 1000f);
		GameContactListener contactListener = new GameContactListener(world, bullets,rocks);
		world.setContactListener(contactListener);
	}

	@Override
	public void render() {
		delta = Gdx.graphics.getDeltaTime();
		world.step(1 / 60f, 6, 2);
		spaceship.update(delta);
		updateBullets(delta);
		updateRock(delta);
		ScreenUtils.clear(0, 0, 0.3f, 1);
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
		spaceship.render(batch);
		batch.end();
	}

	private void updateBullets(float delta) {
		Iterator<Bullet> iterator = bullets.iterator();
		while (iterator.hasNext()) {
			Bullet bullet = iterator.next();
			bullet.update();
			// Remove the bullet if it's off-screen or any other condition you want to check
			if (bullet.getBody().getPosition().x < 0 || bullet.getBody().getPosition().x > camera.viewportWidth ||
					bullet.getBody().getPosition().y < 0 || bullet.getBody().getPosition().y > camera.viewportHeight) {
				iterator.remove();
			}
		}
	}

	private void updateRock(float delta) {
		Iterator<Rock> iterator = rocks.iterator();
		while (iterator.hasNext()) {
			Rock rock = iterator.next();
			rock.update();
			// Remove the bullet if it's off-screen or any other condition you want to check
//			if (rock.getBody().getPosition().x < 0 || rock.getBody().getPosition().x > camera.viewportWidth ||
//					rock.getBody().getPosition().y < 0 || rock.getBody().getPosition().y > camera.viewportHeight) {
//				iterator.remove();
//			}
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

		float angle = MathUtils.atan2(worldY - spaceship.getBody().getPosition().y - (spaceship.getTexture().getHeight() / 2f), worldX - spaceship.getBody().getPosition().x - (spaceship.getTexture().getWidth() / 2f));

		spaceship.setTargetAngle(angle);

		createBullets(worldX, worldY);
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
				spaceship.getBody().getPosition().x + (spaceship.getTexture().getWidth() / 2f),
				spaceship.getBody().getPosition().y + (spaceship.getTexture().getHeight() / 2f),
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
		rock.setSpeed(rockSpeed*delta, (float) Math.toRadians(angle));
		Gdx.app.log("createRocks","rocks.size = "+rocks.size());
	}
}