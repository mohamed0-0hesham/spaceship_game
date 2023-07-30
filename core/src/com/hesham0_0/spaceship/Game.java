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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hesham0_0.spaceship.models.Bullet;
import com.hesham0_0.spaceship.models.Spaceship;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game extends ApplicationAdapter implements InputProcessor {
	private OrthographicCamera camera;
	private final float virtualWidth = 720;
	private final float virtualHeight = 1280;
	private SpriteBatch batch;
	private World world;
	private Spaceship spaceship;
	private List<Bullet> bullets;
	private Box2DDebugRenderer debugRenderer;
	private ShapeRenderer shapeRenderer;

	@Override
	public void create () {
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

		spaceship = new Spaceship(world,virtualWidth,virtualHeight);
		bullets = new ArrayList<>();
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		world.step(1/60f, 6, 2);
		spaceship.update(delta);
		updateBullets(delta);
		ScreenUtils.clear(0, 0, 0.3f, 1);
		batch.begin();
		for (Bullet bullet : bullets) {
			bullet.render(batch);
		}
		spaceship.render(batch);
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
			if (bullet.getBody().getPosition().x < 0 || bullet.getBody().getPosition().x > Gdx.graphics.getWidth() ||
					bullet.getBody().getPosition().y < 0 || bullet.getBody().getPosition().y > Gdx.graphics.getHeight()) {
				iterator.remove();
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
	public void dispose () {
		batch.dispose();
		spaceship.dispose();
		for (Bullet bullet : bullets) {
			bullet.dispose();
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

		float angle = MathUtils.atan2(worldY - spaceship.getBody().getPosition().y-(spaceship.getTexture().getHeight()/ 2f), worldX - spaceship.getBody().getPosition().x- (spaceship.getTexture().getWidth() / 2f));

		spaceship.setTargetAngle(angle);

		createBullets(worldX,worldY);
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
	private void createBullets(float worldX, float worldY){

		float directionX = worldX - spaceship.getBody().getPosition().x;
		float directionY = worldY - spaceship.getBody().getPosition().y;

		float forceMagnitude = 5000;
		float length = (float) Math.sqrt(directionX * directionX + directionY * directionY);
		float forceX = directionX / length * forceMagnitude;
		float forceY = directionY / length * forceMagnitude;

		Bullet bullet = new Bullet(world, spaceship.getBody().getPosition().x + (spaceship.getTexture().getWidth() / 2f)+50, spaceship.getBody().getPosition().y + (spaceship.getTexture().getHeight() / 2f)+50,
				10, 10);
		bullet.applyForce(1000000000, 100000000);
		bullets.add(bullet);
	}
}
