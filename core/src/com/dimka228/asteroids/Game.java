package com.dimka228.asteroids;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.PolygonBatch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dimka228.asteroids.objects.*;
import com.dimka228.asteroids.objects.GameObject.Status;
import com.dimka228.asteroids.objects.GameObject.Type;
import com.dimka228.asteroids.utils.AnyShapeIntersector;
import com.dimka228.asteroids.utils.CollisionUtils;
import com.dimka228.asteroids.utils.Random;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Game extends ApplicationAdapter {
	public final int WIDTH = 1000;
	public final int HEIGHT = 800;
	public final int WORLD_WIDTH = 3000;
	public final int WORLD_HEIGHT = 1000;
	public final int PLAYER_OFFSET = 100;
	public final int GAP = 200;
	public final String TITLE = "игра епта";

	PolygonBatch renderer;
	ShapeDrawer shapeDrawer;
	
	Viewport viewport;
	// Texture background;
	// Sprite backgroundSprite;

	volatile float x = 10;
	private Deque<GameObject> objects;
	private Player player;
	private volatile long count;
	private boolean isRunning;

	private OrthographicCamera camera;


	class World {
		public final float width;
		public final float height;
		public World(float w, float h){
			width = w;
			height = h;
		}
		private Deque<GameObject> objects;
		
	}
	

	public long getTick() {
		return count;
	}

	public void stop() {
		isRunning = false;
	}

	public void addObject(GameObject o) {
		objects.add(o);
	}

	@Override
	public void create() {
		isRunning = true;

		renderer = new PolygonSpriteBatch();
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.drawPixel(0, 0);
		Texture texture = new Texture(pixmap); //remember to dispose of later
		pixmap.dispose();
		TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);
		shapeDrawer = new ShapeDrawer(renderer,region);
		shapeDrawer.setDefaultLineWidth(3);

		objects = new LinkedList<>();
		// backgroundObjects = new LinkedList<>();
		

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		viewport = new ExtendViewport(WIDTH,HEIGHT,camera);
		viewport.apply();

	
		player = new Player(this);

		// objects.add(new Wall(this));
		// background = new Background(this,WIDTH);
		objects.add(player);
		objects.add(new Asteroid(this));
	}

	@Override
   	public void resize(int width, int height){
    	viewport.update(width, height);
    	camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
   	}

	public void updateBackground() {

	}

	@Override
	public void render() {

		ScreenUtils.clear(0, 0, 0, 0);

		camera.position.set(player.getBody().getPosition().x, player.getBody().getPosition().y, 0);
		camera.update();

		renderer.setProjectionMatrix(camera.combined);

		if (Gdx.input.isKeyJustPressed(Input.Keys.P))
			isRunning = !isRunning;
	

		renderer.begin();
		shapeDrawer.rectangle(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
		objects.removeIf((o) -> o.getStatus() == Status.DEAD);

		objects.stream().sorted(new GameObject.SortingComparator()).forEachOrdered((obj) -> {
			
			if (isRunning || obj.getType() == Type.BACKGROUND)
				obj.update();

			obj.render();
		
			if (!isRunning)
				return;
			if (obj.getType() == Type.BACKGROUND)
				return;
			if (player != obj && CollisionUtils.collides(obj.getBody().getShape(), player.getBody().getShape())) {
				player.collide(obj);
				obj.collide(player);
				//System.out.println("player collides obj");
			}
		});

		renderer.end();
		/*
		 * while (iterator.hasNext()) {
		 * GameObject obj = iterator.next();
		 * if (obj.getStatus() == Status.DEAD) {
		 * iterator.remove();
		 * continue;
		 * }
		 * if(isRunning)obj.update();
		 * obj.render();
		 * if(!isRunning) continue;
		 * if (obj.getType() == Type.BACKGROUND)
		 * continue;
		 * // if(player!=obj) System.out.printf("%f %f %f %f\n", obj.getPosition().x,
		 * // obj.getPosition().y, ((Rectangle)obj.getShape()).x,
		 * // ((Rectangle)obj.getShape()).y);
		 * 
		 * if (player != obj && AnyShapeIntersector.overlaps(obj.getShape(),
		 * player.getShape())) {
		 * player.collide(obj);
		 * obj.collide(player);
		 * // obj.setStatus(Status.DEAD);
		 * System.out.println("player collides obj");
		 * }
		 * }
		 */
		if (isRunning)
			count++;

	}

	public PolygonBatch getRenderer() {
		return renderer;
	}

	public ShapeDrawer getDrawer(){
		return shapeDrawer;
	}
	public Camera getCamera() {
		return camera;
	}

	public Player getPlayer() {
		return player;
	}

	public float getLeftBoarder() {
		return player.getBody().getPosition().x - PLAYER_OFFSET;
	}

	public Deque<GameObject> getObjects() {
		return objects;
	}

	@Override
	public void dispose() {
		renderer.dispose();
		// background.dispose();
	}
}
