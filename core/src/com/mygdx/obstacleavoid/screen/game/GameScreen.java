package com.mygdx.obstacleavoid.screen.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.obstacleavoid.ObstacleAvoidGame;
import com.mygdx.obstacleavoid.assets.AssetDescriptors;
import com.mygdx.obstacleavoid.assets.RegionNames;
import com.mygdx.obstacleavoid.common.GameManager;
import com.mygdx.obstacleavoid.config.DifficultyLevel;
import com.mygdx.obstacleavoid.config.GameConfig;
import com.mygdx.obstacleavoid.entity.ObstacleActor;
import com.mygdx.obstacleavoid.entity.PlayerActor;
import com.mygdx.obstacleavoid.screen.menu.MenuScreen;
import com.mygdx.obstacleavoid.util.GdxUtils;
import com.mygdx.obstacleavoid.util.ViewportUtils;
import com.mygdx.obstacleavoid.util.debug.DebugCameraController;

import java.util.Iterator;

public class GameScreen extends ScreenAdapter {

    private static final float PADDING = 20.0f;

    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;
    private final SpriteBatch spriteBatch;
    private final GlyphLayout layout = new GlyphLayout();

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private ShapeRenderer shapeRenderer;

    private OrthographicCamera uiCamera;
    private Viewport uiViewport;
    private BitmapFont font;

    private float obstacleTimer;
    private float scoreTimer;
    private int lives = GameConfig.LIVES_START;
    private int score;
    private int displayScore;
    private Sound hitSound;

    private float startPlayerX = (GameConfig.WORLD_WIDTH - GameConfig.PLAYER_SIZE) / 2;
    private float startPlayerY = GameConfig.PLAYER_SIZE / 2;

    private DebugCameraController debugCameraController;
    private TextureRegion obstacleRegion;
    private TextureRegion backgroundRegion;

    private PlayerActor player;

    private final Array<ObstacleActor> obstacles = new Array<ObstacleActor>();
    private final Pool<ObstacleActor> obstaclePool = Pools.get(ObstacleActor.class);

    public GameScreen(ObstacleAvoidGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        spriteBatch = game.getSpriteBatch();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        stage = new Stage(viewport, spriteBatch);
        stage.setDebugAll(true);

        shapeRenderer = new ShapeRenderer();

        uiCamera = new OrthographicCamera();
        uiViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, uiCamera);
        font = assetManager.get(AssetDescriptors.FONT);

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);

        hitSound = assetManager.get(AssetDescriptors.HIT_SOUND);

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
        TextureRegion playerRegion = gamePlayAtlas.findRegion(RegionNames.PLAYER);
        obstacleRegion = gamePlayAtlas.findRegion(RegionNames.OBSTACLE);
        backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);

        // For static images, it is better to use the Image class (extends Actor) instead of creating an Actor
        Image background = new Image(backgroundRegion);
        background.setSize(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        player = new PlayerActor();
        player.setPosition(startPlayerX, startPlayerY);
        player.setTextureRegion(playerRegion);

        stage.addActor(background);
        stage.addActor(player);
    }

    @Override
    public void render(float delta) {
        // Handle debugCamera input and apply configuration to our camera
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        update(delta);

        // Clear the screen
        GdxUtils.clearScreen();

        viewport.apply();
        renderGamePlay();

        uiViewport.apply();
        renderUI();

        viewport.apply();
        renderDebug();

        if (isGameOver()){
            game.setScreen(new MenuScreen(game));
        }
    }

    private void update(float delta){
        if (isGameOver()){
            return;
        }

        createNewObstacle(delta);
        removePassedObstacles();

        updateScore(delta);
        updateDisplayScore(delta);

        if (isPlayerCollidingWithObstacle()){
            lives--;

            if (isGameOver()){
                GameManager.INSTANCE.updateHighScore(score);
            }else{
                restart();
            }
        }
    }

    private void createNewObstacle(float delta) {
        obstacleTimer += delta;

        if (obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME) {
            float min = 0f;
            float max = GameConfig.WORLD_WIDTH - GameConfig.OBSTACLE_SIZE;
            float obstacleX = MathUtils.random(min, max);
            float obstacleY = GameConfig.WORLD_HEIGHT;

            //Obstacle obstacle = new Obstacle();
            ObstacleActor obstacle = obstaclePool.obtain();
            DifficultyLevel difficultyLevel = GameManager.INSTANCE.getDifficultyLevel();
            obstacle.setYSpeed(difficultyLevel.getObstacleSpeed());
            obstacle.setPosition(obstacleX, obstacleY);
            obstacle.setTextureRegion(obstacleRegion);

            obstacles.add(obstacle);
            stage.addActor(obstacle);
            // reset the obstacle timer
            obstacleTimer = 0.0f;
        }
    }

    private void removePassedObstacles() {
        if (obstacles.size > 0) {
            float minObstacleY = (0 - GameConfig.OBSTACLE_SIZE);
            ObstacleActor obstacle = obstacles.first();
            if (obstacle.getY() < minObstacleY) {
                obstacles.removeValue(obstacle, true);
                // Removes actor from parent / stage
                obstacle.remove();
                // Returning to pool
                obstaclePool.free(obstacle);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        uiViewport.update(width, height, true);

        ViewportUtils.debugPixelPerUnit(viewport);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private void renderGamePlay() {
        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();
        drawGamePlay();
        spriteBatch.end();

        stage.act();
        stage.draw();
    }

    private void drawGamePlay() {
    }

    private void renderUI() {
        spriteBatch.setProjectionMatrix(uiCamera.combined);

        spriteBatch.begin();

        // draw lives
        String livesText = "LIVES: " + lives;
        layout.setText(font, livesText);
        font.draw(spriteBatch, layout, PADDING, GameConfig.HUD_HEIGHT - layout.height);

        // draw score
        String scoreText = "SCORE: " + score;
        layout.setText(font, scoreText);
        font.draw(spriteBatch, layout, GameConfig.HUD_WIDTH - layout.width - PADDING, GameConfig.HUD_HEIGHT - layout.height);

        drawGamePlay();

        spriteBatch.end();
    }

    private void renderDebug() {
        // draw grid
        ViewportUtils.drawGrid(viewport, shapeRenderer);
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    // == private methods ==
    private void restart() {
        Iterator<ObstacleActor> iterator = obstacles.iterator();
        while(iterator.hasNext()){
            ObstacleActor obstacle = iterator.next();

            // remove obstacles from stage (parent)
            obstacle.remove();

            // return to pool
            obstaclePool.free(obstacle);

            iterator.remove();
        }
        player.setPosition(startPlayerX, startPlayerY);
    }

    private boolean isPlayerCollidingWithObstacle() {
        for (ObstacleActor obstacle : obstacles) {
            if (!obstacle.isHit() && obstacle.isPlayerColliding(player)) {
                hitSound.play();
                return true;
            }
        }
        return false;
    }

    private void updateScore(float deltaTime) {
        scoreTimer += deltaTime;

        if (scoreTimer >= GameConfig.SCORE_MAX_TIME) {
            score += MathUtils.random(0, 5);
            scoreTimer = 0.0f;
        }
    }

    private void updateDisplayScore(float deltaTime) {
        if (displayScore < score) {
            displayScore = Math.min(score, displayScore + (int) (60 * deltaTime));   // 60 frames per second
        }
    }
}
