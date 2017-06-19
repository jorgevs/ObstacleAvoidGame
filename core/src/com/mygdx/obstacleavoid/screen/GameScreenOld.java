package com.mygdx.obstacleavoid.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.obstacleavoid.assets.AssetPaths;
import com.mygdx.obstacleavoid.config.DifficultyLevel;
import com.mygdx.obstacleavoid.config.GameConfig;
import com.mygdx.obstacleavoid.entity.Obstacle;
import com.mygdx.obstacleavoid.entity.Player;
import com.mygdx.obstacleavoid.util.GdxUtils;
import com.mygdx.obstacleavoid.util.ViewportUtils;
import com.mygdx.obstacleavoid.util.debug.DebugCameraController;

@Deprecated
public class GameScreenOld implements Screen {
    private static final Logger LOGGER = new Logger(GameScreenOld.class.getName(), Logger.DEBUG);

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private OrthographicCamera hudCamera;
    private Viewport hudViewport;

    private SpriteBatch batch;
    private BitmapFont font;
    private final GlyphLayout glyphLayout = new GlyphLayout();

    private Player player;
    private Array<Obstacle> obstacles;

    private float obstacleTimer;
    private float scoreTimer;
    private int lives = GameConfig.LIVES_START;
    private int score;
    private int displayScore;
    private DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;

    private DebugCameraController debugCameraController;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal(AssetPaths.UI_FONT));

        player = new Player();
        // player's initial position
        player.setPosition(GameConfig.WORLD_WIDTH / 2, 1);

        // obstacles
        obstacles = new Array<Obstacle>();

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
    }

    @Override
    public void render(float deltaTime) {
        // update camera (is not wrapped inside the alive conditions, because we
        // want to be able to control the camera even when the game is over)
        debugCameraController.handleDebugInput(deltaTime);
        debugCameraController.applyTo(camera);

        // update world
        //if(alive) {
            update(deltaTime);
        //}

        // clear screen
        GdxUtils.clearScreen();

        // render ui/hud
        renderUi();

        // render debug graphics
        renderDebug();
    }

    private void update(float deltaTime) {
        if(isGameOver()){
            LOGGER.debug("Game Over!!!");
            return;
        }

        updatePlayer();
        updateObstacles(deltaTime);
        updateScore(deltaTime);
        updateDisplayScore(deltaTime);

        if(isPlayerCollidingWithObstacle()){
            LOGGER.debug("Collision detected.");
            lives--;
        }
    }

    private void renderUi(){
        batch.setProjectionMatrix(hudCamera.combined);

        batch.begin();
        drawUi();
        batch.end();
    }

    private void drawUi(){
        String livesText = "LIVES: " + lives;
        glyphLayout.setText(font, livesText);
        font.draw(batch, livesText, 20, GameConfig.HUD_HEIGHT - glyphLayout.height);

        String scoreText = "SCORE: " + displayScore;
        glyphLayout.setText(font, scoreText);
        font.draw(batch, scoreText, GameConfig.HUD_WIDTH - glyphLayout.width - 20, GameConfig.HUD_HEIGHT - glyphLayout.height);
    }

    private boolean isPlayerCollidingWithObstacle(){
        for(Obstacle obstacle : obstacles){
            if(!obstacle.isHit() && obstacle.isPlayerColliding(player)){
                return true;
            }
        }

        return false;
    }

    private void updatePlayer() {
        //LOGGER.debug("playerX: " + player.getX() + " playerY: " + player.getY());
        player.update();

        blockPlayerFromLeavingTheWorld();
    }

    private void blockPlayerFromLeavingTheWorld() {
        float playerX = MathUtils.clamp(player.getX(), (player.getWidth() / 2), (GameConfig.WORLD_WIDTH - player.getWidth() / 2));
        float playerY = MathUtils.clamp(player.getY(), (player.getHeight() / 2), (GameConfig.WORLD_HEIGHT - player.getHeight() / 2));

        player.setPosition(playerX, playerY);
    }

    private void updateObstacles(float delta){
        for (Obstacle obstacle : obstacles){
            obstacle.update();
        }

        createNewObstacle(delta);
    }

    private void createNewObstacle(float delta){
        obstacleTimer += delta;

        if(obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME){
            float min = 0.0f;
            float max = GameConfig.WORLD_WIDTH;
            float obstacleX = MathUtils.random(min, max);
            float obstacleY = GameConfig.WORLD_HEIGHT;

            Obstacle obstacle = new Obstacle();
            obstacle.setSpeedY(difficultyLevel.getObstacleSpeed());
            obstacle.setPosition(obstacleX, obstacleY);

            obstacles.add(obstacle);
            // reset the obstacle timer
            obstacleTimer = 0.0f;
        }
    }

    private void updateScore(float deltaTime){
        scoreTimer += deltaTime;

        if(scoreTimer >= GameConfig.SCORE_MAX_TIME){
            score += MathUtils.random(0,5);
            scoreTimer = 0.0f;
        }
    }

    private void updateDisplayScore(float deltaTime) {
        if(displayScore < score){
            displayScore = Math.min(score, displayScore + (int)(60 * deltaTime));   // 60 frames per second

        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);

        ViewportUtils.debugPixelPerUnit(viewport);
    }

    private void renderDebug() {
        renderer.setProjectionMatrix(camera.combined);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        drawDebug();
        renderer.end();

        ViewportUtils.drawGrid(viewport, renderer);
    }

    private void drawDebug() {
        player.drawDebug(renderer);

        for (Obstacle obstacle : obstacles){
            obstacle.drawDebug(renderer);
        }
    }

    private boolean isGameOver(){
        return lives <= 0;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        // important
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();

        batch.dispose();
        font.dispose();
    }
}
